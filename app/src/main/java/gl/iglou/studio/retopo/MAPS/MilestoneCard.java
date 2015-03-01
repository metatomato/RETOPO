package gl.iglou.studio.retopo.MAPS;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.InputStream;

import gl.iglou.studio.retopo.R;

/**
 * Created by metatomato on 01.03.15.
 */
public class MilestoneCard extends RelativeLayout {

    private ImageView mImageTrace;
    private TextView mTextTitle;
    private TextView mTextComment;
    private TextView mTextTime;
    private TextView mtextDate;

    private Context mContext;

    public MilestoneCard(Context context) {
        super(context);

        mContext = context;
        init();
    }

    public MilestoneCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MilestoneCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.milestone_card, this);

        mImageTrace = (ImageView) findViewById(R.id.image_trace);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextComment = (TextView) findViewById(R.id.text_comment);
        mTextTime = (TextView) findViewById(R.id.text_time);
        mtextDate = (TextView) findViewById(R.id.text_date);
    }

    public void setMileStoneCard(Long date, String title, String comment, String photo) {
        setTime(date);
        setDate(date);
        setTitle(title);
        setTextComment(comment);
        setImageTrace(photo);
    }

    public void setTitle(String title) {
        mTextTitle.setText(title);
    }

    public void setTextComment(String comment) {
        mTextComment.setText(comment);
    }

    public void setImageTrace(String image) {
        int imageId = this.getResources().getIdentifier(
                image.substring(0, image.lastIndexOf('.'))
                , "raw"
                , mContext.getPackageName()
        );

        InputStream imageIS = this.getResources().openRawResource(imageId);
        Bitmap myImage = BitmapFactory.decodeStream(imageIS);

        mImageTrace.setImageBitmap(myImage);
    }

    public void setTime(Long date) {
        mTextTime.setText(android.text.format.DateFormat.getTimeFormat(mContext).format(date * 1000L));
    }

    public void setDate(Long date) {
        mtextDate.setText(android.text.format.DateFormat.getDateFormat(mContext).format(date * 1000L));
    }
}
