package demo.otote.cn.translationdemo.module;

import android.graphics.Bitmap;

/**
 * @author chaochaowu
 * @Description : MainContract
 * @class : MainContract
 * @time Create at 6/4/2018 4:21 PM
 */


public interface MainContract {

    interface View{
        void updateUI(String s);
    }

    interface Presenter{
        void getAccessToken();
        void getRecognitionResultByImage(Bitmap bitmap);
    }

}
