package com.example.cine;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FragmentTrailer extends DialogFragment {
    public static final String TAG = FragmentTrailer.class.getSimpleName();
    private WebView wvTrailer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout customViewContainer;

    private String trailer;
    public FragmentTrailer(String trailer) {
        this.trailer=trailer;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trailer, null);
        Dialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(v)
                .setCancelable(true)  // Permite que el usuario cierre el diálogo manualmente
                .create();

        wvTrailer = v.findViewById(R.id.WVtrailer);
        customViewContainer = v.findViewById(R.id.customViewContainer);

        WebSettings webSettings = wvTrailer.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false); // Permite autoplay

        wvTrailer.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                customView = view;
                customViewCallback = callback;

                // Ocultar el WebView original y agregar la vista de pantalla completa
                wvTrailer.setVisibility(View.GONE);
                customViewContainer.setVisibility(View.VISIBLE);
                customViewContainer.addView(customView, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

                // Ocultar barra de estado y navegación
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                // Forzar orientación horizontal
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) return;

                // Restaurar el WebView
                wvTrailer.setVisibility(View.VISIBLE);
                customViewContainer.setVisibility(View.GONE);
                customViewContainer.removeView(customView);
                customViewCallback.onCustomViewHidden();
                customView = null;

                // Restaurar la orientación original
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

                // Restaurar la barra de estado y navegación
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        });

        wvTrailer.setWebViewClient(new WebViewClient());

        // Cargar el video de YouTube en el WebView
        String trailerID = "JpUd4BS7yI0"; // ID del video
        String embedHtml = "<html><body style='margin:0;padding:0;overflow:hidden;'>" +
                "<iframe width='100%' height='100%' " +
                "src='https://www.youtube.com/embed/" + trailer + "?fs=1&autoplay=1' " +
                "frameborder='0' allow='accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture; fullscreen' " +
                "allowfullscreen></iframe></body></html>";

        wvTrailer.loadData(embedHtml, "text/html", "utf-8");

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
