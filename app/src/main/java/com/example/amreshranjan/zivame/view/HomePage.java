package com.example.amreshranjan.zivame.view;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.amreshranjan.zivame.R;
import com.example.amreshranjan.zivame.adapter.ViewPagerAdapter;
import com.example.amreshranjan.zivame.asynchronous_task.GetFeatureListTask;
import com.example.amreshranjan.zivame.model.Feature;

import java.util.ArrayList;

import it.sephiroth.android.library.tooltip.Tooltip;

public class HomePage extends AppCompatActivity {

    private ViewPager imageViewPager;
    private LinearLayout dotIndicatorLayout, featureListLayout;
    private int[] productImages = {R.drawable.product_one, R.drawable.product_two}; // dummy image
    private ImageView[] ivArrayDotsPager;
    private TextView productName, productPrice;
    private ArrayList<Feature> featureList;
    private final int MAX_COLUMN_VALUE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initialiseComponents();
    }

    /**
     * Initialise all views
     */
    private void initialiseComponents(){
        productName = (TextView) findViewById(R.id.product_name_text);
        productPrice = (TextView) findViewById(R.id.product_price_text);
        featureListLayout = (LinearLayout) findViewById(R.id.features_layout);
        dotIndicatorLayout = (LinearLayout) findViewById(R.id.dot_indicator);
        imageViewPager = (ViewPager) findViewById(R.id.image_view_pager);

        imageViewPager.setAdapter(new ViewPagerAdapter(HomePage.this, productImages));
        setupPagerIndicatorDots();
        ivArrayDotsPager[0].setImageResource(R.drawable.selected_view);
        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < ivArrayDotsPager.length; i++) {
                    ivArrayDotsPager[i].setImageResource(R.drawable.unselected_view);
                }
                ivArrayDotsPager[position].setImageResource(R.drawable.selected_view);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Read data from json file
        new GetFeatureListTask(this, new IResponse() {
            @Override
            public void onPostExecuteData(ArrayList<Feature> features) {
                featureList = features;
                populateFeatureList();
            }
        }).execute();
    }

    /**
     * Creating new linearLayout to add in to parent layout with feature text.
     * @param count
     * @return
     */
    private LinearLayout getLayout(int count){
        int appendSeparator = 0;
        LinearLayout subParentLayout = new LinearLayout(this);
        subParentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        subParentLayout.setOrientation(LinearLayout.HORIZONTAL);
        subParentLayout.setGravity(Gravity.CENTER);
        int indexToConsider = count* MAX_COLUMN_VALUE;
        int lastIndex = indexToConsider + MAX_COLUMN_VALUE;
        for(int j = indexToConsider; j<lastIndex; j++){
            if(j < featureList.size()){
                subParentLayout.addView(createFeatureText(featureList.get(j).getName(), featureList.get(j).getDescription(), appendSeparator));
                appendSeparator++;
            }
        }
        return subParentLayout;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Using third party library to show description in tooltip.
            launchToolTip(view, view.getTag().toString());
            setDefaultColor();
            ((TextView) view).setTextColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        }
    };

    /**
     * Set default color for all texts.
     */
    private void setDefaultColor(){
        for (int i = 0; i < featureListLayout.getChildCount(); i++){
            View layout = featureListLayout.getChildAt(i);
            if(layout instanceof LinearLayout){
                LinearLayout layout1 = (LinearLayout) layout;
                for (int j = 0; j < layout1.getChildCount(); j ++){
                    TextView textView = (TextView)layout1.getChildAt(j);
                    textView.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPurple));
                }
            }

        }
    }

    /**
     * Create dynamic textView for features.
     * @param name
     * @param description
     * @param sperator
     * @return
     */
    private TextView createFeatureText(String name, String description, int sperator){
        TextView textView = new TextView(this);
        textView.setLayoutParams(setParams());
        textView.setAllCaps(true);
        textView.setText(sperator > 0 ? " |  " + name : name);
        textView.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPurple));
        textView.setTag(description);
        textView.setOnClickListener(listener);

        return textView;
    }

    /**
     * Return remainder after dividing features count by maximum number of column.
     * @return
     */
    private int hasRemainder(){
        return  featureList.size() % MAX_COLUMN_VALUE;

    }

    /**
     * Launches tool tips with some predefined style.
     * @param view
     * @param tipContent
     */
    public void launchToolTip(View view, String tipContent) {
        Tooltip.make(this,
                new Tooltip.Builder()
                        .anchor(view, Tooltip.Gravity.BOTTOM)
                        .closePolicy(new Tooltip.ClosePolicy()
                                .outsidePolicy(true, false), 10000)

                        .text(tipContent)
                        .maxWidth(800)
                        .withArrow(true)
                        .withStyleId(R.style.ToolTipLayoutCustomStyle)
                        .withOverlay(false).build() //withOverlay(True) will start showing with blink that where u clicked
        ).show();
    }

    /**
     * Populate feature list and add textviews dynamically to layout.
     */
    private void populateFeatureList(){
        int count = 0;
        if(featureList != null && featureList.size() > 0){
            int num = featureList.size()/3;
            if(hasRemainder() > 0){
                num += 1;
            }
            while (count < num){
                featureListLayout.addView(getLayout(count));
                count++;
            }
        }
    }

    /**
     * Create LinearLayout dynamically
     * @return
     */
    private LinearLayout.LayoutParams setParams(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 0.3f;

        return params;
    }

    /**
     * Setup dot indicator for viewpager.
     */
    private void setupPagerIndicatorDots() {
        ivArrayDotsPager = new ImageView[productImages.length];
        for (int i = 0; i < ivArrayDotsPager.length; i++) {
            ivArrayDotsPager[i] = new ImageView(HomePage.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            ivArrayDotsPager[i].setLayoutParams(params);
            ivArrayDotsPager[i].setImageResource(R.drawable.unselected_view);
            //ivArrayDotsPager[i].setAlpha(0.4f);
            ivArrayDotsPager[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    view.setAlpha(1);
                }
            });
            dotIndicatorLayout.addView(ivArrayDotsPager[i]);
            dotIndicatorLayout.bringToFront();
        }
    }

}
