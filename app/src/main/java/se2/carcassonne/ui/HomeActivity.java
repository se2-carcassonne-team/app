package se2.carcassonne.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import se2.carcassonne.R;
import se2.carcassonne.databinding.HomeActivityBinding;
import se2.carcassonne.helper.animation.AnimationHelper;
import se2.carcassonne.helper.mapper.MapperHelper;
import se2.carcassonne.helper.resize.FullscreenHelper;

import se2.carcassonne.repository.PlayerRepository;
import se2.carcassonne.viewmodel.PlayerViewModel;

public class HomeActivity extends AppCompatActivity {
    HomeActivityBinding binding;
    private PlayerViewModel playerViewModel;
    private final MapperHelper mapperHelper = new MapperHelper();
    private WebView webView;
    private LinearLayout buttonLayout;
    private ImageView logo;
    private TextView textView2;
    private ProgressBar progressBar;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FullscreenHelper.setFullscreenAndImmersiveMode(this);
        
        logo = findViewById(R.id.logo_animation);
        logo.setVisibility(View.INVISIBLE);

        AnimationHelper.fadeIn(logo,2000,null);

        PlayerRepository.resetInstance();
        playerViewModel = new PlayerViewModel();

        showChooseUsernameDialog();
        playerViewModel.getMessageLiveData().observe(this, message -> binding.textView2.setText(String.format(getString(R.string.welcome_homescreen), mapperHelper.getPlayerName(message))));
        binding.buttonPlayGame.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, GameLobbyActivity.class);
            startActivity(intent);
        });

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript
        webView.addJavascriptInterface(this, "Android"); // Add a JavaScript interface
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Inject a JavaScript function to add a close button
                webView.loadUrl("javascript:(function() { " +
                        "let button = document.createElement('button'); " +
                        "button.innerHTML = 'Close'; " +
                        "button.style.position = 'fixed'; " +
                        "button.style.top = '10px'; " +
                        "button.style.right = '10px'; " +
                        "button.style.backgroundColor = 'yellow'; " + // Set the background color to yellow
                        "button.style.color = 'black'; " + // Set the text color to black
                        "button.style.fontSize = '20px'; " + // Increase the font size
                        "button.style.padding = '10px'; " + // Add padding
                        "button.style.border = '2px solid black'; " + // Add a black border
                        "button.addEventListener('click', function() { " +
                        "Android.closeWebView(); " +
                        "}); " +
                        "document.body.appendChild(button); " +
                        "})()");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress < 100 && progressBar.getVisibility() == View.GONE) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        webView.setVisibility(View.GONE); // Hide the WebView initially

        progressBar = findViewById(R.id.progressBar); // Replace with the id of your ProgressBar
        progressBar.setVisibility(View.GONE); // Hide the ProgressBar initially


        buttonLayout = findViewById(R.id.linearLayoutButtons); // Replace with the id of your LinearLayout containing the buttons
        textView2 = findViewById(R.id.textView2); // Replace with the id of your welcome message

        binding.buttonShowGameRules.setOnClickListener(view -> {
            String url = "https://wikicarpedia.com/car/Base_game_(1st_edition)"; // Replace with your specific URL
            webView.loadUrl(url);
            webView.setVisibility(View.VISIBLE); // Show the WebView
            buttonLayout.setVisibility(View.GONE); // Hide the buttons
            logo.setVisibility(View.GONE); // Hide the logo
            textView2.setVisibility(View.GONE); // Hide the welcome message
        });
    }

    // Add a method to be called from JavaScript
    @JavascriptInterface
    public void closeWebView() {
        runOnUiThread(() -> {
            webView.setVisibility(View.GONE); // Hide the WebView
            buttonLayout.setVisibility(View.VISIBLE); // Show the buttons
            logo.setVisibility(View.VISIBLE); // Show the logo
            textView2.setVisibility(View.VISIBLE); // Show the welcome message
        });
    }



    private void showChooseUsernameDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ChooseUsernameDialogFragment dialogFragment = new ChooseUsernameDialogFragment(playerViewModel);
        dialogFragment.show(fragmentManager, "ChooseUsernameDialogFragment");
    }
}
