package yuntao.com.overscollview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by jomeslu on 15-8-28.
 */
public class HeadFragment extends Fragment {

    private ViewParent mViewParent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.head, null);
        initView(view);
        return view;

    }

    private void initView(View view) {
        mViewParent= (ViewParent) view.findViewById(R.id.vPager);
    }
}
