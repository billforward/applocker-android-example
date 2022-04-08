package com.applocker.sample.ui.subscriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.applocker.sample.MainActivity;
import com.applocker.sample.R;
import com.applocker.sample.databinding.FragmentSubscriptionsBinding;

public class SubscriptionsFragment extends Fragment {

    private FragmentSubscriptionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ViewGroup viewGroup = root.findViewById(R.id.subscriptions_fragment);

        var applockerLoginHandler = ((MainActivity)requireActivity()).getApplockerLoginHandler();
        WebView webView = applockerLoginHandler.getWebView();

        viewGroup.addView(webView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return root;
    }

    @Override
    public void onDestroyView() {
        View root = binding.getRoot();
        ViewGroup viewGroup = root.findViewById(R.id.subscriptions_fragment);
        viewGroup.removeAllViews();
        super.onDestroyView();
        binding = null;
    }
}