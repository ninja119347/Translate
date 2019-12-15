package demo.otote.cn.translationdemo.module;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import demo.otote.cn.translationdemo.apiservice.BaiduOCRService;
import demo.otote.cn.translationdemo.bean.AccessTokenBean;
import demo.otote.cn.translationdemo.bean.RecognitionResultBean;
import demo.otote.cn.translationdemo.utils.RegexUtils;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author chaochaowu
 * @Description : MainPresenter
 * @class : MainPresenter
 * @time Create at 6/4/2018 4:21 PM
 */


public class MainPresenter implements MainContract.Presenter{
    private StringBuilder s;
    private MainContract.View mView;
    private BaiduOCRService baiduOCRService;

    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private static final String API_KEY = "t1OtjZdeKqS29IbZiBbvtU8A";
    private static final String SECRET_KEY = "rNBGP4mfSgwqpYq7d5tksxx9UXRL1NCt";
    private static final String ACCESS_TOKEN = "24.4de5b615d3574023e874783e6e2c5cc3.2592000.1578379889.282335-17963556";

    public MainPresenter(MainContract.View mView) {

        this.mView = mView;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://aip.baidubce.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        baiduOCRService = retrofit.create(BaiduOCRService.class);

    }


    @Override
    public void getAccessToken() {

        baiduOCRService.getAccessToken(CLIENT_CREDENTIALS,API_KEY,SECRET_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AccessTokenBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AccessTokenBean accessTokenBean) {
                        Log.e("Access token",accessTokenBean.getAccess_token());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Access token","error");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    @Override
    public void getRecognitionResultByImage(Bitmap bitmap) {

        String encodeResult = bitmapToString(bitmap);

        baiduOCRService.getRecognitionResultByImage(ACCESS_TOKEN,encodeResult)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RecognitionResultBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(RecognitionResultBean recognitionResultBean) {
                        Log.e("onnext",recognitionResultBean.toString());

                        ArrayList<String> wordList = new ArrayList<>();
                        List<RecognitionResultBean.WordsResultBean> wordsResult = recognitionResultBean.getWords_result();
                        for (RecognitionResultBean.WordsResultBean words:wordsResult) {
                            wordList.add(words.getWords());
                        }
                        Log.e("onnext_1",wordList.toString());
                        ArrayList<String> numbs = RegexUtils.getNumbs(wordList);
                        Log.e("onnext_1",numbs.toString());
                         s = new StringBuilder();
                         mView.updateUI(wordList.toString().substring(1,wordList.toString().length()-1));
//                        for (String numb : numbs) {
//                            s.append(numb + "\n");
//                        }

                       // mView.updateUI(s.toString());

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("onerror",e.toString());
                        mView.updateUI("failed");
                    }

                    @Override
                    public void onComplete() {
                       // mView.updateUI("compelete"+s.toString());
                    }
                });

    }



    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }


}
