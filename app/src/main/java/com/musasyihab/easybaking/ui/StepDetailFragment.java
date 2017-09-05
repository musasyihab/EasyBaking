package com.musasyihab.easybaking.ui;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musasyihab.easybaking.R;
import com.musasyihab.easybaking.model.RecipeModel;
import com.musasyihab.easybaking.model.StepModel;

import java.util.List;

/**
 * Created by musasyihab on 9/5/17.
 */

public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {
    public static final String RECIPE = "RECIPE";
    public static final String CURRENT_POS = "CURRENT_POS";

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private SimpleExoPlayer mExoPlayer;

    private FrameLayout mPlayerLayout;
    private ImageView mImageView;
    private SimpleExoPlayerView mPlayerView;
    private Button mBtnNext;
    private Button mBtnPrev;
    private TextView mDescription;

    private RecipeModel recipe;
    private List<StepModel> stepList;
    private StepModel currentStep;
    private int currentPosition = 0;
    private String sourceURL;

    private Activity activity;

    public StepDetailFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState!=null){
            if(savedInstanceState.containsKey(RECIPE)){
                String sRecipe = savedInstanceState.getString(RECIPE);
                recipe = new Gson().fromJson(sRecipe,
                        new TypeToken<RecipeModel>() {}.getType());
            }
            if(savedInstanceState.containsKey(CURRENT_POS)){
                currentPosition = savedInstanceState.getInt(CURRENT_POS, 0);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String sRecipe = new Gson().toJson(recipe);
        outState.putString(RECIPE, sRecipe);
        outState.putInt(CURRENT_POS, currentPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        final View rootView = inflater.inflate(R.layout.activity_step_detail, container, false);

        mPlayerLayout = (FrameLayout) rootView.findViewById(R.id.step_detail_video_layout);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.step_detail_video);
        mImageView = (ImageView) rootView.findViewById(R.id.step_detail_thumb);
        mBtnNext = (Button) rootView.findViewById(R.id.step_detail_next);
        mBtnPrev = (Button) rootView.findViewById(R.id.step_detail_prev);
        mDescription = (TextView) rootView.findViewById(R.id.step_detail_desc);

        if(recipe == null) {
            return rootView;
        }

        stepList = recipe.getSteps();

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition++;
                loadView();
            }
        });

        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition--;
                loadView();
            }
        });

        loadView();

        return rootView;
    }

    public void setRecipe(RecipeModel recipe){
        this.recipe = recipe;
    }

    public void setCurrentPos(int currentPos){
        currentPosition = currentPos;
    }

    private void loadView(){

        if(mMediaSession!=null) {
            releasePlayer();
            mMediaSession.setActive(false);
        }

        if(currentPosition==0){
            mBtnNext.setEnabled(true);
            mBtnPrev.setEnabled(false);
        } else if (currentPosition==stepList.size()-1){
            mBtnNext.setEnabled(false);
            mBtnPrev.setEnabled(true);
        } else {
            mBtnNext.setEnabled(true);
            mBtnPrev.setEnabled(true);
        }

        currentStep = recipe.getSteps().get(currentPosition);

        mDescription.setText(currentStep.getDescription());

        mPlayerLayout.setVisibility(View.VISIBLE);

        if(currentStep.getVideoURL()!=null && !currentStep.getVideoURL().isEmpty()){
            sourceURL = currentStep.getVideoURL();
            mPlayerView.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);

            // Load the question mark as the background image until the user answers the question.
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                    (getResources(), R.drawable.ic_restaurant_menu_green));

            // Initialize the Media Session.
            initializeMediaSession();

            // Initialize the player.
            initializePlayer(Uri.parse(sourceURL));

        } else if (currentStep.getThumbnailURL()!=null && !currentStep.getThumbnailURL().isEmpty()){
            sourceURL = currentStep.getThumbnailURL();
            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);

            if(isURLImage()) {
                Glide.with(activity).load(sourceURL)
                        .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_restaurant_menu_white)
                        .into(mImageView);
            }
        } else {
            mPlayerView.setVisibility(View.GONE);
            mImageView.setVisibility(View.GONE);
            mPlayerLayout.setVisibility(View.GONE);
        }
    }

    private boolean isURLImage(){
        boolean isImage = sourceURL.endsWith(".jpg") || sourceURL.endsWith(".jpeg") || sourceURL.endsWith(".bmp") || sourceURL.endsWith(".png")
                || sourceURL.endsWith(".gif");

        return isImage;
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(activity, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity);

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(activity,
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (activity, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (activity, 0, new Intent(activity, StepDetailActivity.class), 0);

        builder.setContentTitle(recipe.getName())
                .setContentText(getString(R.string.step)+" " +(currentPosition + 1))
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_restaurant_menu_green)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(0,1));


        mNotificationManager = (NotificationManager) activity.getSystemService(activity.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }


    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(activity, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(activity, "ClassicalMusicQuiz");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    activity, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if(mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        }
        if(mExoPlayer!=null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
        mExoPlayer = null;
    }

    /**
     * Release the player when the fragment is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if(mMediaSession!=null)
            mMediaSession.setActive(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
        if(mMediaSession!=null)
            mMediaSession.setActive(false);
    }

    // ExoPlayer Event Listeners

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }
}