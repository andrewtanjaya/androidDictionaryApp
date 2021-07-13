package edu.bluejack20_1.SOwhaDZ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import edu.bluejack20_1.SOwhaDZ.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    // Array
    public int[] slide_images = {
            R.drawable.ic_translate_it,
            R.drawable.ic_speak_it,
            R.drawable.ic_ocr
    };

    public String[] slide_headings = {
            "TRANSLATE IT ! ",
            "SPEAK IT !",
            "SCAN IT !"
    };

    public String[] slide_desc = {
            "Just type it and you can understand it",
            "Just speak and we will translate it ",
            "Take a picture, and walaaa you got it"
    };



    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        slide_headings[0] = view.getResources().getText(R.string.intro_translate_it).toString();
        slide_headings[1] = view.getResources().getText(R.string.intro_speak_it).toString();
        slide_headings[2] = view.getResources().getText(R.string.intro_scan_it).toString();

        slide_desc[0] = view.getResources().getText(R.string.intro_desc1).toString();
        slide_desc[1] = view.getResources().getText(R.string.intro_desc2).toString();
        slide_desc[2] = view.getResources().getText(R.string.intro_desc3).toString();


        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeading = (TextView) view.findViewById(R.id.slide_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.slide_description);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_desc[position]);

        container.addView(view);

        return view;
        }

@Override
public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
        }
        }
