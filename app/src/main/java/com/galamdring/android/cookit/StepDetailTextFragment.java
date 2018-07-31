package com.galamdring.android.cookit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.galamdring.android.cookit.Data.Step;

public class StepDetailTextFragment extends Fragment {
    private static final String KEY_FOR_PARCEL_IN_BUNDLE = "Key for the step in the bundle.";
    private TextView Description;
    private Step MyStep;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyStep=getArguments()!=null?getArguments().getParcelable(KEY_FOR_PARCEL_IN_BUNDLE):null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_detail_text,container,false);
        Description = view.findViewById(R.id.stepDetailDescription);
        if(Description!=null && MyStep !=null) PopulateUI();
        return view;
    }

    private void PopulateUI(){
        Description.setText(MyStep.getDescription());
    }

    public void setMyStep(Step myStep) {
        MyStep = myStep;
        if(Description!=null) PopulateUI();
    }

    public static StepDetailTextFragment instanceOf(Step myStep){
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FOR_PARCEL_IN_BUNDLE,myStep);
        StepDetailTextFragment fragment = new StepDetailTextFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
