package com.raitra.studio;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Base64;
import android.webkit.JavascriptInterface;
import java.io.*;

public class MainActivity extends Activity {
    private WebView webView;
    private ValueCallback<Uri[]> fileCallback;
    private static final int FILE_CHOOSER = 22;
    private PermissionRequest pendingRequest;
    private File exTmp; private FileOutputStream exOut; private String exName, exMime;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        webView = new WebView(this);
        setContentView(webView);
        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true); s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true); s.setAllowContentAccess(true);
        webView.setKeepScreenOn(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface public void begin(String n, String m) { try { exName = n; exMime = m; exTmp = new File(getCacheDir(), "export.tmp"); exOut = new FileOutputStream(exTmp); } catch (Exception e) {} }
            @JavascriptInterface public void chunk(String b64) { try { exOut.write(Base64.decode(b64, Base64.DEFAULT)); } catch (Exception e) {} }
            @JavascriptInterface public void end() { try { exOut.close(); } catch (Exception e) {}
                runOnUiThread(() -> { Intent i = new Intent(Intent.ACTION_CREATE_DOCUMENT); i.addCategory(Intent.CATEGORY_OPENABLE); i.setType(exMime); i.putExtra(Intent.EXTRA_TITLE, exName); startActivityForResult(i, 31); }); }
        }, "RaitraBridge");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> {
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) request.grant(request.getResources());
                    else { pendingRequest = request; requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 23); }
                });
            }
            @Override public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> callback, FileChooserParams params) {
                if (fileCallback != null) fileCallback.onReceiveValue(null);
                fileCallback = callback;
                Intent i = params.createIntent();
                try { startActivityForResult(i, FILE_CHOOSER); } catch (Exception e) { fileCallback = null; Toast.makeText(MainActivity.this, "Impossible d'ouvrir les fichiers", Toast.LENGTH_SHORT).show(); return false; }
                return true;
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
    }
    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        super.onRequestPermissionsResult(requestCode, permissions, results);
        if (requestCode == 23 && pendingRequest != null) {
            if (results.length > 0 && results[0] == PackageManager.PERMISSION_GRANTED) pendingRequest.grant(pendingRequest.getResources()); else pendingRequest.deny();
            pendingRequest = null;
        }
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 31) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null && exTmp != null) {
                try (InputStream in = new FileInputStream(exTmp); OutputStream out = getContentResolver().openOutputStream(data.getData())) {
                    byte[] b = new byte[65536]; int n; while ((n = in.read(b)) > 0) out.write(b, 0, n);
                    Toast.makeText(this, "Fichier enregistré", Toast.LENGTH_LONG).show();
                } catch (Exception e) { Toast.makeText(this, "Échec de l'enregistrement", Toast.LENGTH_LONG).show(); }
            }
            return;
        }
        if (requestCode == FILE_CHOOSER && fileCallback != null) {
            Uri[] result = (resultCode == RESULT_OK && data != null) ? WebChromeClient.FileChooserParams.parseResult(resultCode, data) : null;
            fileCallback.onReceiveValue(result); fileCallback = null;
        }
    }
    @Override public void onBackPressed() { if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }
}
