package com.oil.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UrlWebView extends WebViewClient {

    @Override

    public boolean shouldOverrideUrlLoading(WebView view, String url){

    // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边

       view.loadUrl(url);

       return true;

       }

}
