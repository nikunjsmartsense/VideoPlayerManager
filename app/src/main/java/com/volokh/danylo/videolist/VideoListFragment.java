package com.volokh.danylo.videolist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.volokh.danylo.videolist.adapter.VideoListViewAdapter;
import com.volokh.danylo.videolist.adapter.items.ListItem;
import com.volokh.danylo.videolist.adapter.items.LocalVideoItem;
import com.volokh.danylo.videolist.adapter.visibilityutils.ItemsPositionGetter;
import com.volokh.danylo.videolist.adapter.visibilityutils.SingleListViewItemActiveCalculator;
import com.volokh.danylo.videolist.player.manager.SingleVideoPlayerManager;
import com.volokh.danylo.videolist.adapter.items.VideoItem;
import com.volokh.danylo.videolist.player.manager.VideoPlayerManager;
import com.volokh.danylo.videolist.adapter.visibilityutils.ListItemsVisibilityCalculator;
import com.volokh.danylo.videolist.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class VideoListFragment extends Fragment implements AbsListView.OnScrollListener, SingleListViewItemActiveCalculator.Callback<ListItem>,ItemsPositionGetter {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = VideoListFragment.class.getSimpleName();

    private final ArrayList<VideoItem> mList = new ArrayList<>();
    private final ListItemsVisibilityCalculator mVideoVisibilityCalculator = new SingleListViewItemActiveCalculator(this, mList);
    private final VideoPlayerManager mVideoPlayerManager = new SingleVideoPlayerManager(mVideoVisibilityCalculator);

    private int mScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
    private ListView mListView;
    private VideoListViewAdapter mVideoListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            mList.add(new LocalVideoItem("Batman vs Dracula.mp4", getActivity().getAssets().openFd("Batman vs Dracula.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("DZIDZIO.mp4", getActivity().getAssets().openFd("DZIDZIO.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Tyrion speech during trial.mp4.mp4", getActivity().getAssets().openFd("Tyrion speech during trial.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Grasshopper.mp4", getActivity().getAssets().openFd("Grasshopper.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Neo vs. Luke Skywalker.mp4.mp4", getActivity().getAssets().openFd("Neo vs. Luke Skywalker.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("ne_lyubish.mp4", getActivity().getAssets().openFd("ne_lyubish.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("ostanus.mp4", getActivity().getAssets().openFd("ostanus.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("O_TORVALD_Ne_vona.mp4", getActivity().getAssets().openFd("O_TORVALD_Ne_vona.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Nervy_cofe.mp4.mp4", getActivity().getAssets().openFd("Nervy_cofe.mp4"), mVideoPlayerManager));
//            mList.add(new DirectLinkVideoItem("https://duw49sogxuf9v.cloudfront.net/d/c/MlYeMAJVR21vBwdhCzE"));
            mList.add(new LocalVideoItem("Batman vs Dracula.mp4", getActivity().getAssets().openFd("Batman vs Dracula.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("DZIDZIO.mp4", getActivity().getAssets().openFd("DZIDZIO.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Tyrion speech during trial.mp4.mp4", getActivity().getAssets().openFd("Tyrion speech during trial.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Grasshopper.mp4", getActivity().getAssets().openFd("Grasshopper.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Neo vs. Luke Skywalker.mp4.mp4", getActivity().getAssets().openFd("Neo vs. Luke Skywalker.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("ne_lyubish.mp4", getActivity().getAssets().openFd("ne_lyubish.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("ostanus.mp4", getActivity().getAssets().openFd("ostanus.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("O_TORVALD_Ne_vona.mp4", getActivity().getAssets().openFd("O_TORVALD_Ne_vona.mp4"), mVideoPlayerManager));
            mList.add(new LocalVideoItem("Nervy_cofe.mp4.mp4", getActivity().getAssets().openFd("Nervy_cofe.mp4"), mVideoPlayerManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        View rootView = inflater.inflate(R.layout.fragment_video_list_view, container, false);

        mListView = (ListView) rootView.findViewById(R.id.list_view);
        mVideoListViewAdapter = new VideoListViewAdapter(mVideoPlayerManager, getActivity(), mList);
        mListView.setAdapter(mVideoListViewAdapter);
        mListView.setOnScrollListener(this);
        mVideoListViewAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mList.isEmpty()){
            // need to call this method from list view handler in order to have filled list

            mListView.post(new Runnable() {
                @Override
                public void run() {

                    mVideoVisibilityCalculator.onScrollStateIdle(
                            VideoListFragment.this,
                            mListView.getFirstVisiblePosition(),
                            mListView.getLastVisiblePosition());

                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mVideoPlayerManager.resetMediaPlayer();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
        if(scrollState == SCROLL_STATE_IDLE && !mList.isEmpty()){
            mVideoVisibilityCalculator.onScrollStateIdle(VideoListFragment.this, view.getFirstVisiblePosition(), view.getLastVisiblePosition());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(!mList.isEmpty()){
            mVideoVisibilityCalculator.onScroll(VideoListFragment.this, firstVisibleItem, visibleItemCount, mScrollState);
        }
    }

    @Override
    public void onActivateNewCurrentItem(ListItem newListItem, View currentView, int position) {
        if(SHOW_LOGS) Logger.v(TAG, "onActivateNewCurrentItem, newListItem " + newListItem);
        newListItem.setActive(currentView, position);
    }

    @Override
    public void onDeactivateCurrentItem(ListItem itemToDeactivate, View view, int position) {
        if(SHOW_LOGS) Logger.v(TAG, "onDeactivateCurrentItem, itemToDeactivate " + itemToDeactivate);
        itemToDeactivate.deactivate(view, position);
    }

    @Override
    public View getChildAt(int position) {
        return mListView.getChildAt(position);
    }

    @Override
    public int indexOfChild(View view) {
        return mListView.indexOfChild(view);
    }

    @Override
    public int getChildCount() {
        return mListView.getChildCount();
    }

    @Override
    public int getLastVisiblePosition() {
        return mListView.getLastVisiblePosition();
    }

    @Override
    public int getFirstVisiblePosition() {
        return mListView.getFirstVisiblePosition();
    }
}