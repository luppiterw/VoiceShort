package utils.richeditor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hughie on 15/10/26.
 */
public class RichEditor extends WebView {

    public enum Type {
        BOLD,
        ITALIC,
        SUBSCRIPT,
        SUPERSCRIPT,
        STRIKETHROUGH,
        UNDERLINE,
        H1,
        H2,
        H3,
        H4,
        H5,
        H6
    }

    /**
     * 供外部调用的接口类
     * 编辑器文字改变时，调用onTextChange方法，
     * 可在外部实现onTextChange方法，达到外部的目的
     *
     * */
    public interface OnTextChangeListener {

        void onTextChange(String text);
    }
    /**
     * 供外部调用的接口类
     * 内部预留，未使用，可删除
     * 可在外部实现onStateChangeListener方法，达到外部的目的
     *
     * */
    public interface OnDecorationStateListener {

        void onStateChangeListener(String text, List<Type> types);
    }

    /**
     * 供外部调用的接口类
     * 加载完成时，调用onAfterInitialLoad方法，
     * 可在外部实现onAfterInitialLoad方法，达到外部的目的
     *
     * */
    public interface AfterInitialLoadListener {

        void onAfterInitialLoad(boolean isReady);
    }

    ///< url 地址（此处使用本地地址）对应于assets/editor.html部署
    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    ///< assets 中rich_editor.js监控 todo 了解js相关事件回调及处理
    private static final String CALLBACK_SCHEME = "re-callback://";
    ///< assets 中rich_editor.js监控 todo 了解js相关事件回调及处理
    private static final String STATE_SCHEME = "re-state://";
    ///< js editor加载完成的标识，未加载完成时，对editor的一系列操作无法进行，处于等待状态
    private boolean isReady = false;
    private String mHtmlContents;
    private ArrayList<String> mHtmlContentsArray = new ArrayList<String>();

    private OnTextChangeListener mTextChangeListener;
    private OnDecorationStateListener mDecorationStateListener;
    private AfterInitialLoadListener mLoadListener;

    private TestJS mTestJS;
    /**
     * 创建一个单线程的线程池用于处理editor的exec任务，即当editor未加载成功时，将对editor的操作保存至该线程池中
     * */
    private static ExecutorService sThreadPool = Executors.newSingleThreadExecutor();

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setVerticalScrollBarEnabled(true);
        setHorizontalScrollBarEnabled(true);

        ///< 用于android stuidio中预览
        if(isInEditMode())
            return;

        getSettings().setJavaScriptEnabled(true);
        mTestJS = new TestJS();
        addJavascriptInterface(this,"TestJS");

        getProgress();

//        getBackground().setAlpha(2);;
        /**
         * 需要监视加载进度的时候，可创建自己的WebChromeClient类，并重载onProgressChanged方法，
         * 再webview.setWebChromeClient(new MyWebChromeClient())即可。
         */
        setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                Log.d("Hughie","WebChromeClient onProgressChanged calling.");
            }

        });
        setWebViewClient(new WebViewClient() {

            /**
             * 加载完成时调用
             * */
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("Hughie","onPageFinished url="+url);
                isReady = url.equalsIgnoreCase(SETUP_HTML);
                if (mLoadListener != null) {
                    mLoadListener.onAfterInitialLoad(isReady);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String decode;
                Log.d("Hughie","shouldOverrideUrlLoading url="+url);
                try {
                    decode = URLDecoder.decode(url, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // No handling
                    return false;
                }

                if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                    callback(decode);
                    return true;
                } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                    stateCheck(decode);
                    return true;
                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        loadUrl(SETUP_HTML);

        applyAttributes(context, attrs);
    }

    public void setOnTextChangeListener(OnTextChangeListener listener) {
        Log.d("Hughie","setOnTextChangeListener");
        mTextChangeListener = listener;
    }

    public void setOnDecorationChangeListener(OnDecorationStateListener listener) {
        Log.d("Hughie","setOnDecorationChangeListener");
        mDecorationStateListener = listener;
    }

    public void setOnInitialLoadListener(AfterInitialLoadListener listener) {
        Log.d("Hughie","setOnInitialLoadListener");
        mLoadListener = listener;
    }

    public int getCursorLine()
    {
        int ret = -1;
        return ret;
    }

    private void callback(String text) {
        Log.d("Hughie","callback text=" + text);
        assignContent(text.replaceFirst(CALLBACK_SCHEME, ""));
//        mHtmlContents = text.replaceFirst(CALLBACK_SCHEME, "");
        if (mTextChangeListener != null) {
//            Log.d("Hughie","callback mTextChangeListener=" + mTextChangeListener);
            mTextChangeListener.onTextChange(mHtmlContents);
        }
    }

    private void stateCheck(String text) {

        String state = text.replaceFirst(STATE_SCHEME, "").toUpperCase(Locale.ENGLISH);
        Log.d("Hughie", "stateCheck text=" + text + " state=" + state);
        List<Type> types = new ArrayList<>();
        for (Type type : Type.values()) {
//            Log.d("Hughie","    stateCheck for state=" + state + " type.name=" + type.name());
            if (TextUtils.indexOf(state, type.name()) != -1) {
                types.add(type);
            }
        }

        if (mDecorationStateListener != null) {
            mDecorationStateListener.onStateChangeListener(state, types);
        }
    }

    private void applyAttributes(Context context, AttributeSet attrs) {

        /**test self-defined attrs*/
//        final int[] selfAttrsArray = R.styleable.RichEditor;
//        TypedArray test = context.obtainStyledAttributes(attrs,selfAttrsArray);
//        int edtorGravity = test.getInt(0,NO_ID);
//        Log.d("Hughie","edtorGravity=" +edtorGravity);

        /**Use system gravity to set alignment.
         * Self-defined attrs maybe used here when necessary
         * TODO
         * */

        final int[] attrsArray = new int[] {
                android.R.attr.gravity
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        int gravity = ta.getInt(0, NO_ID);
        /**
         * js中的editor应该是默认居中的，
         * 此处没有处理组合的情况，比如在xml中设置了android:gravity="left|top"，则在此处找不到对应，
         * 奇怪的是，这里找不到对应，但是在显示的时候还是会生效
         * 我的猜测是，因为继承了WebView控件，其应该在内部有一些基础的属性设置，所以直接在xml中配置会生效
         * 这里的自定义方式，则会覆盖内部设置。
         * todo 这里需要添加完整组合的处理，防止带来一些意外结果
         * */
        Log.d("Hughie","applyAttributes gravity=" + gravity);
        switch (gravity) {
            case Gravity.LEFT:
                Log.d("Hughie","applyAttributes Gravity.LEFT=" + Gravity.LEFT);
                exec("javascript:RE.setTextAlign(\"left\")");
                break;
            case Gravity.RIGHT:
                Log.d("Hughie","applyAttributes Gravity.RIGHT=" + Gravity.RIGHT);
                exec("javascript:RE.setTextAlign(\"right\")");
                break;
            case Gravity.TOP:
                Log.d("Hughie","applyAttributes Gravity.TOP=" + Gravity.TOP);
                exec("javascript:RE.setVerticalAlign(\"top\")");
                break;
            case Gravity.BOTTOM:
                Log.d("Hughie","applyAttributes Gravity.BOTTOM=" + Gravity.BOTTOM);
                exec("javascript:RE.setVerticalAlign(\"bottom\")");
                break;
            case Gravity.CENTER_VERTICAL:
                Log.d("Hughie","applyAttributes Gravity.CENTER_VERTICAL=" + Gravity.CENTER_VERTICAL);
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                break;
            case Gravity.CENTER_HORIZONTAL:
                Log.d("Hughie","applyAttributes Gravity.CENTER_HORIZONTAL=" + Gravity.CENTER_HORIZONTAL);
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
            case Gravity.CENTER:
                Log.d("Hughie","applyAttributes Gravity.CENTER=" + Gravity.CENTER);
                exec("javascript:RE.setVerticalAlign(\"middle\")");
                exec("javascript:RE.setTextAlign(\"center\")");
                break;
        }

        ta.recycle();
    }

    /**
     * Common interface for setting current text contents, changing some symbols to html format.
     * Only handle '\n' here. Maybe some other characters need to be explained later.
     * @param text Input String, normal text format.
     * */
    public void setText(String text)
    {
        setHtml(text.replaceAll("\n","\\<br>"));
    }
    /**
     * Common interface for getting current text contents, changing some html symbols to text format.
     * Only handle '<br>' here. Maybe some other characters need to be explained later.
     * @return Format text string
     * */
    public String getText()
    {
        return mHtmlContents.replaceAll("\\<br>", "\n");
    }
    /**
     * Get content line count.
     * @return Line count.
     * */
    public int getLineCount()
    {
        return mHtmlContentsArray.size();
    }

    /**
     * Forbidden to use this function to modify html contents directly.
     * @param contents Html format contents.
     * */
    private void setHtml(String contents)
    {
        Log.d("Hughie", "setHtml contents=" + contents);
        if (contents == null)
        {
            contents = "";
        }
        try
        {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            ///todo do something
        }
        assignContent(contents);
    }
    /**
     * Assign mHtmlContents and do something necessary when assigning.
     * @param contents New mHtmlContents.
     * */
    private void assignContent(String contents)
    {
        if(mHtmlContents == contents)
            return;
        mHtmlContents = contents;
        mHtmlContentsArray.clear();
        String[] array = contents.split("\\<br>",-1);
        for(int i = 0; i < array.length; i++)
        {
            ///< except for last <br>
            if(i < 1 || i != array.length - 1)
                mHtmlContentsArray.add(array[i]);
        }

    }
    /**
     * Clear editor data.
     *
     * */
    public void clearEditorData()
    {
        setHtml("");
    }

//    public void setHtml(String contents) {
//        Log.d("Hughie","setHtml contents=" + contents);
//        if (contents == null) {
//            contents = "";
//        }
//        try {
//            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "');");
//        } catch (UnsupportedEncodingException e) {
//            // No handling
//        }
//        mHtmlContents = contents;
//    }
//
//    public String getHtml() {
//        return mHtmlContents;
//    }

    public void setEditorFontColor(int color) {
        String hex = convertHexColorString(color);
        exec("javascript:RE.setBaseTextColor('" + hex + "');");
    }

    public void setEditorFontSize(int px) {
        exec("javascript:RE.setBaseFontSize('" + px + "px');");
    }

    @Override public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        exec("javascript:RE.setPadding('" + left + "px', '" + top + "px', '" + right + "px', '" + bottom
                + "px');");
    }

    @Override public void setPaddingRelative(int start, int top, int end, int bottom) {
        // still not support RTL.
        setPadding(start, top, end, bottom);
    }

    public void setEditorBackgroundColor(int color) {
        setBackgroundColor(color);
    }

    @Override public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
    }

    @Override public void setBackgroundResource(int resid) {
        Bitmap bitmap = Utils.decodeResource(getContext(), resid);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    @Override public void setBackground(Drawable background) {
        Bitmap bitmap = Utils.toBitmap(background);
        String base64 = Utils.toBase64(bitmap);
        bitmap.recycle();

        exec("javascript:RE.setBackgroundImage('url(data:image/png;base64," + base64 + ")');");
    }

    public void setBackground(String url) {
        exec("javascript:RE.setBackgroundImage('url(" + url + ")');");
    }

    public void setEditorWidth(int px) {
        exec("javascript:RE.setWidth('" + px + "px');");
    }

    public void setEditorHeight(int px) {
        exec("javascript:RE.setHeight('" + px + "px');");
    }

    public void setPlaceholder(String placeholder) {
        exec("javascript:RE.setPlaceholder('" + placeholder + "');");
    }

    public void loadCSS(String cssFile) {
        String jsCSSImport = "(function() {" +
                "    var head  = document.getElementsByTagName(\"head\")[0];" +
                "    var link  = document.createElement(\"link\");" +
                "    link.rel  = \"stylesheet\";" +
                "    link.type = \"text/css\";" +
                "    link.href = \"" + cssFile + "\";" +
                "    link.media = \"all\";" +
                "    head.appendChild(link);" +
                "}) ();";
        exec("javascript:" + jsCSSImport + "");
    }

    public void undo() {
        exec("javascript:RE.undo();");
    }

    public void redo() {
        exec("javascript:RE.redo();");
    }

    public void setBold() {
        exec("javascript:RE.setBold();");
    }

    public void setItalic() {
        exec("javascript:RE.setItalic();");
    }

    public void setSubscript() {
        exec("javascript:RE.setSubscript();");
    }

    public void setSuperscript() {
        exec("javascript:RE.setSuperscript();");
    }

    public void setStrikeThrough() {
        exec("javascript:RE.setStrikeThrough();");
    }

    public void setUnderline() {
        exec("javascript:RE.setUnderline();");
    }

    public void setTextColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextColor('" + hex + "');");
    }

    public void setTextBackgroundColor(int color) {
        exec("javascript:RE.prepareInsert();");

        String hex = convertHexColorString(color);
        exec("javascript:RE.setTextBackgroundColor('" + hex + "');");
    }

    public void removeFormat() {
        exec("javascript:RE.removeFormat();");
    }

    public void setHeading(int heading) {
        exec("javascript:RE.setHeading('" + heading + "');");
    }

    public void setIndent() {
        exec("javascript:RE.setIndent();");
    }

    public void setOutdent() {
        exec("javascript:RE.setOutdent();");
    }

    public void setAlignLeft() {
        exec("javascript:RE.setJustifyLeft();");
    }

    public void setAlignCenter() {
        exec("javascript:RE.setJustifyCenter();");
    }

    public void setAlignRight() {
        exec("javascript:RE.setJustifyRight();");
    }

    public void setBlockquote() {
        exec("javascript:RE.setBlockquote();");
    }

    public void insertImage(String url, String alt) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertImage('" + url + "', '" + alt + "');");
    }

    public void insertLink(String href, String title) {
        exec("javascript:RE.prepareInsert();");
        exec("javascript:RE.insertLink('" + href + "', '" + title + "');");
    }

    public void focusEditor() {
        requestFocus();
        exec("javascript:RE.focus();");
    }

    public void clearFocusEditor() {
        exec("javascript:RE.blurFocus();");
    }

    private String convertHexColorString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    private void exec(String trigger) {
        if (isReady) {
            load(trigger);
        } else {
            new WaitLoad(trigger).executeOnExecutor(sThreadPool);
        }
    }

    private void load(String trigger) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(trigger, null);
        } else {
            loadUrl(trigger);
        }
    }

    /**
     * 等待editor加载的任务
     * todo 具体实现可扩展
     * */
    private class WaitLoad extends AsyncTask<Void, Void, Void> {

        private String mTrigger;

        public WaitLoad(String trigger) {
            super();
            mTrigger = trigger;
        }

        @Override protected Void doInBackground(Void... params) {
            while (!RichEditor.this.isReady) {
                sleep(100);
            }
            return null;
        }

        @Override protected void onPostExecute(Void aVoid) {
            load(mTrigger);
        }

        private synchronized void sleep(long ms) {
            try {
                wait(ms);
            } catch (InterruptedException ignore) {
            }
        }
    }


    public void testJSInterface() {
        exec("javascript:RE.testFun();");
//        loadUrl("javascript:RE.testFun()");
        Log.d("Hughie","testJSInterface");
    }

    @JavascriptInterface
    public void printTest(String result)
    {
        Log.d("Hughie", "[RichEditor] printTest result=" + result);
    }
    @JavascriptInterface
    public String getText(String text)
    {
        Log.d("Hughie","getText=["+text+"]");
        return text;
    }
    @JavascriptInterface
    public String getSelection(String selection)
    {
        Log.d("Hughie","getSelection=["+selection+"]");
        return selection;
    }
}
