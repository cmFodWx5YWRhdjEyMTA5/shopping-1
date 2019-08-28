//package com.nahuo.quicksale;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.OnScrollListener;
//import android.support.v7.widget.StaggeredGridLayoutManager;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.webkit.WebView;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import com.nahuo.Dialog.DomDialog;
//import com.nahuo.bean.SearchPanelBean;
//import com.nahuo.constant.IDsConstant;
//import com.nahuo.library.controls.BottomMenu;
//import com.nahuo.library.controls.LoadingDialog;
//import com.nahuo.library.helper.DisplayUtil;
//import com.nahuo.library.helper.FunctionHelper;
//import com.nahuo.library.helper.GsonHelper;
//import com.nahuo.library.helper.ImageUrlExtends;
//import com.nahuo.library.utils.TimeUtils;
//import com.nahuo.quicksale.Topic.PostDetailActivity;
//import com.nahuo.quicksale.activity.ItemPreview1Activity;
//import com.nahuo.quicksale.adapter.Bookends;
//import com.nahuo.quicksale.adapter.PinHuoDetailAdapter;
//import com.nahuo.quicksale.adapter.PinHuoDetailAdapter.Listener;
//import com.nahuo.quicksale.adapter.SortFilterAdpater;
//import com.nahuo.quicksale.api.HttpRequestHelper;
//import com.nahuo.quicksale.api.HttpRequestListener;
//import com.nahuo.quicksale.api.QuickSaleApi;
//import com.nahuo.quicksale.api.RequestMethod;
//import com.nahuo.quicksale.api.ShopSetAPI;
//import com.nahuo.quicksale.common.Const;
//import com.nahuo.quicksale.common.ListUtils;
//import com.nahuo.quicksale.common.SpManager;
//import com.nahuo.quicksale.controls.refreshlayout.IRefreshLayout.RefreshCallBack;
//import com.nahuo.quicksale.controls.refreshlayout.SwipeRefreshLayoutEx;
//import com.nahuo.quicksale.customview.ItemJCVideoPlayerStandard;
//import com.nahuo.quicksale.eventbus.BusEvent;
//import com.nahuo.quicksale.eventbus.EventBusId;
//import com.nahuo.quicksale.jcvideoplayer_lib.JCVideoPlayer;
//import com.nahuo.quicksale.model.ItemShopCategory;
//import com.nahuo.quicksale.model.PinHuoModel;
//import com.nahuo.quicksale.model.ResultData;
//import com.nahuo.quicksale.model.ShopItemListModel;
//import com.nahuo.quicksale.model.quicksale.RecommendModel;
//import com.nahuo.quicksale.util.FocusSave;
//import com.squareup.picasso.Picasso;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import de.greenrobot.event.EventBus;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static com.nahuo.quicksale.api.RequestMethod.QuickSaleMethod.RECOMMEND_SHOP_ITEMS;
//
///**
// * 拼货详情列表
// * Created by ZZB on 201510/15.
// */
//public class PinHuoDetailFragment extends BaseFragment implements RefreshCallBack, View.OnClickListener {
//    private static final String TAG = PinHuoDetailFragment.class.getSimpleName();
//    //    private static String EXTRA_PIN_HUO_MODEL = "EXTRA_PIN_HUO_MODEL";
////    private static String EXTRA_PIN_HUO_OVERED = "EXTRA_PIN_HUO_OVERED";
//    private static int PAGE_INDEX = 0;
//    private static final int PAGE_SIZE = 20;
//    private boolean isTasking = false, mIsRefresh = true;
//    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();
//    private RecyclerView mRecyclerView;
//    private SwipeRefreshLayoutEx mRefreshLayout;
//    private RecommendModel mRecommendModel;
//    private PinHuoDetailAdapter mNewItemAdapter;
//    private Bookends<PinHuoDetailAdapter> mNewItemBookends;
//    protected LoadingDialog mLoadingDialog;
//    private PinHuoModel mPinHuoModel;
//    private ImageView mIvCover, iv_cover_yg, iv_cover_ended, mIvNewItemIcon, iv_cover_pining;
//    private View mOveredTips, mNextActivitysView;
//    private TextView tv_stall_name, tv_shop_intro, txt_next_yg, mTvShopName, mTvShopDesc, mTvHH, mTvH, mTvMM, mTvM, mTvSS, mTvS, mTvF, mTvDay, mTvDayDesc, mTvOveredTips, btn_focus;//mTvTitle, mTvContent,
//    private WebView mTvNextTips, tv_overed_webview;
//    private TextView mTvFHH, mTvFH, mTvFMM, mTvFM, mTvFSS, mTvFS, mTvFF, mTvFDay, mTvFDayDesc, mTvfOveredTips;
//    private CircleImageView shopIcon;
//    private MyCountDownTimer mCountDownTimer;
//    private ProgressBar mLoadMorePB;
//    private TextView mLoadMoreTxt, mTvNextActivity;
//    private View mFloatHeadView, mVScrollToTop, mEmptyView;
//    private ItemJCVideoPlayerStandard video_player;
//    private View layout_need_hide;
//    private int mLastPos;
//    private int n;
//    private boolean mNeedToClearKeyword;
//    private TextView tvDealSort, tvShopCat, tvCollectSort, tvMustDealSort;
//    private List<ItemShopCategory> mItemShopCategories;
//    //    private TextView tvDealSort1,tvCreateTimeSort1,tvCollectSort1,tvMustDealSort1;
//    private Const.SortIndex mSortIndex = Const.SortIndex.DefaultDesc;
//    private int mColorBlue = Color.parseColor("#38A8FE");
//    private int mColorGray = Color.parseColor("#717171");
//    static PinHuoDetailFragment fragmentInstance;
//    private int mShopCatId = -1;
//    private int mCurrentPs = 0;
//    private boolean isOvered = false;
//    private Context mContext;
//    private int displayMode = 0;
//    private List<ShopItemListModel> PassItems = new ArrayList<>();
//    public LinearLayout ll_bottom, ll_bottomx, ll_bottomx2;
//    // private HorizontalScrollView hs_bottomx;
//    public int newCount = 0;
//    public int oldeCount = 0;
//    public List<ShopItemListModel> firstPassItems = new ArrayList<>();
//    private String currentMenu = "";
//    private int currentMenuID;
//    private int sortBy = -1;
//    private boolean isFocus = false;
//    public static String FIRST_FOCUS_DETAI_PRE = "FIRST_FOCUS_DETAI_PRE";
//    private int ID = -1, QID = -1;
//    private LinearLayout lladd;
//    private long countdownDuration = 0;
//    static public PinHuoDetailListActivity act;
//    private View layout_out_head, top_bottomx;
//    private int head_height;
//    private DrawerLayout mDrawerLayout;
//    private View drawer_content;
//    private TextView tv_draw_layout_ok, tv_draw_layout_clear;
//    private RecyclerView recycler_drawer;
//    private SortFilterAdpater sortFilterAdpater;
//    private List<SearchPanelBean.PanelsBeanX> Panels = new ArrayList<>();
//    private SearchPanelBean searchPanelBean = null;
//    private boolean first_Post_Panels = true;
//    private int lastPosition = 0;
//    private int lastOffset = 0;
//    private LinearLayoutManager linearLayoutManager;
//    private String filterValues = "";//筛选数据
//
//    public static PinHuoDetailFragment getInstance(PinHuoModel model, boolean overed, Context context, PinHuoDetailListActivity activity) {
//        fragmentInstance = new
//                PinHuoDetailFragment();
//        fragmentInstance.isOvered = overed;
//        fragmentInstance.mPinHuoModel = model;
//        fragmentInstance.mShopCatId = -1;
//        fragmentInstance.mCurrentPs = 0;
//        fragmentInstance.isTasking = false;
//        fragmentInstance.mContext = context;
//        act = activity;
//        fragmentInstance.mItemShopCategories = null;
//        return fragmentInstance;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        mContentView = inflater.inflate(R.layout.frgm_pin_huo_detail, container, false);
////        isOvered = getArguments().getBoolean(EXTRA_PIN_HUO_OVERED);
//        try {
//            initViews();
//            initData();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return mContentView;
//    }
//
//    private void initData() {
//        if (getActivity() == null) {
//            return;
//        }
//        mRefreshLayout.manualRefresh();
//        if (mLoadingDialog == null) {
//            mLoadingDialog = new LoadingDialog(mActivity);
//        }
//    }
//
//    private void bindData() {
//        if (mRecommendModel.getInfo().getActivityType().equals("拼货")) {
//            lladd.setVisibility(View.VISIBLE);
//            if (mRecommendModel.getInfo().getOpenStatu() != null && mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
//                mIvNewItemIcon.setVisibility(View.VISIBLE);
//            } else {
//                mIvNewItemIcon.setVisibility(View.GONE);
//            }
//        } else {
//            lladd.setVisibility(View.GONE);
//            mIvNewItemIcon.setVisibility(View.GONE);
//        }
//        mNewItemAdapter.setChengTuanCount(mRecommendModel.getInfo().getChengTuanCount());
//        mNewItemAdapter.part2title = mRecommendModel.getInfo().getPart2Title();
//        countdownDuration = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
//        if (mRecommendModel.getInfo().ShowFllow) {
//            if (btn_focus != null)
//                btn_focus.setVisibility(View.VISIBLE);
//        } else {
//            if (btn_focus != null)
//                btn_focus.setVisibility(View.GONE);
//        }
//        String url = ImageUrlExtends.getImageUrl(mRecommendModel.getInfo().getAppCover(), 20);
//        //mRecommendModel.getInfo().setVideo("http://nahuo-video-server.b0.upaiyun.com/0/171016/131525960265864128.mp4");
//        if (TextUtils.isEmpty(mRecommendModel.getInfo().getVideo())) {
//            if (video_player != null)
//                video_player.setVisibility(View.GONE);
//        } else {
//            if (video_player != null) {
//                video_player.setVisibility(View.VISIBLE);
//                video_player.setUp(mRecommendModel.getInfo().getVideo()
//                        , JCVideoPlayer.SCREEN_LAYOUT_LIST
//                        , mRecommendModel.getInfo().getName(), mRecommendModel, layout_need_hide);
//                Picasso.with(mAppContext).load(url).placeholder(R.drawable.empty_photo).into(video_player.thumbImageView);
//            }
//        }
//        if (mRecommendModel.getInfo() != null) {
//            if (act.tvTitleCenter != null) {
//                act.tvTitleCenter.setText(mRecommendModel.getInfo().getName() + "");
//            }
//            if (!TextUtils.isEmpty(mRecommendModel.getInfo().getStallName())) {
//                tv_stall_name.setText(mRecommendModel.getInfo().getStallName());
//                tv_stall_name.setVisibility(View.VISIBLE);
//            } else {
//                tv_stall_name.setVisibility(View.GONE);
//            }
//            if (!TextUtils.isEmpty(mRecommendModel.getInfo().getSignature())) {
//                tv_shop_intro.setText(mRecommendModel.getInfo().getSignature());
//                tv_shop_intro.setVisibility(View.GONE);
//            } else {
//                tv_shop_intro.setVisibility(View.GONE);
//            }
//        }
//        Picasso.with(mAppContext).load(url).placeholder(R.drawable.empty_photo).into(mIvCover);
//        mTvShopDesc.setText(mRecommendModel.getInfo().getDescription());
//        mTvShopName.setText(mRecommendModel.getInfo().getShopUserName());
//        String shopLogo = Const.getShopLogo(mRecommendModel.getInfo().getShopUserID());
//        Picasso.with(mAppContext).load(shopLogo).placeholder(R.drawable.empty_photo).into(shopIcon);
//
//        if (mIsRefresh) {
//            if (mRecommendModel.getInfo().isStart()) {//开始状态 // && mRecommendModel.getInfo().isHasNewItems()
//                if (mRecommendModel.getInfo().getActivityType().equals("拼货")) {
//                    //普通场
//                    if (mRecommendModel.getInfo().getOpenStatu().equals("预告")) {
////                        mTvOveredTips.setVisibility(View.VISIBLE);
////                        .setText("预计将于今晚"+mRecommendModel.getInfo().getTimes()+"点开拼");
//                        //中部html
//                        mTvNextTips.setVisibility(View.VISIBLE);
//                        StringBuilder html = new StringBuilder();
//                        html.append("<html>"
//                                + "<head>"
//                                + "<style type=\"text/css\">"
//                                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                                + "<div class=wp-item-detail-format>");
//                        html.append(mRecommendModel.getInfo().getCenterContent());
//
//                        html.append("</div>" + "</body>" + "</html>");
//                        mTvNextTips.getSettings().setDefaultTextEncodingName("UTF -8");
//                        mTvNextTips.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                        mTvNextTips.setVerticalScrollBarEnabled(false);
//                        //右上角绿色图标
//                        iv_cover_yg.setVisibility(View.VISIBLE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                        //加载往期好货的数据
//                    } else if (mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
//                        //右上角新款图标
//                        mIvNewItemIcon.setVisibility(View.VISIBLE);
//                        //倒计时
//                        mCountDownTimer = new MyCountDownTimer(countdownDuration);
//                        mCountDownTimer.start();
//                        mOveredTips.setVisibility(View.GONE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.tv_overed_tips).setVisibility(View.GONE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.VISIBLE);
//                        if (countdownDuration > 0) {
//                            if (lladd != null)
//                                lladd.setVisibility(View.VISIBLE);
//                            mCountDownTimer = new MyCountDownTimer(countdownDuration);
//                            mCountDownTimer.start();
//                        } else {
//                            if (lladd != null)
//                                lladd.setVisibility(View.GONE);
//                        }
//                    } else if (mRecommendModel.getInfo().getOpenStatu().equals("已结束")) {
//                        //中部html
//                        mTvNextTips.setVisibility(View.VISIBLE);
//                        StringBuilder html = new StringBuilder();
//                        html.append("<html>"
//                                + "<head>"
//                                + "<style type=\"text/css\">"
//                                + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                                + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                                + "<div class=wp-item-detail-format>");
//                        html.append(mRecommendModel.getInfo().getCenterContent());
//                        html.append("</div>" + "</body>" + "</html>");
//                        mTvNextTips.getSettings().setDefaultTextEncodingName("UTF -8");
//                        mTvNextTips.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                        mTvNextTips.setVerticalScrollBarEnabled(false);
//                        //右上角结束图标  大邮戳没有， 只有小图
//                        iv_cover_ended.setVisibility(View.VISIBLE);
//                        headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                    }
//                } else {//聚合场，按照目前逻辑不变
//                    mIvNewItemIcon.setVisibility(View.GONE);
//                    if (lladd != null) {
//                        lladd.setVisibility(View.GONE);
//                    }
//                }
//            } else {//结束状态
//                //本场已结束
//                mTvOveredTips.setVisibility(View.VISIBLE);
//                mTvfOveredTips.setText("本场已结束");
//                mTvOveredTips.setText("本场已结束");
//                tv_overed_webview.setVisibility(View.VISIBLE);
//                StringBuilder html = new StringBuilder();
//                html.append("<html>"
//                        + "<head>"
//                        + "<style type=\"text/css\">"
//                        + ".wp-item-detail-format img,.wp-item-detail-format table{ max-width:100%;width:auto!important;height:auto!important; }"
//                        + "*{margin:0px; padding:0px;}" + "</style>" + "</head>" + "<body>"
//                        + "<div class=wp-item-detail-format>");
//                html.append(mRecommendModel.getInfo().getCenterContent());
//                html.append("</div>" + "</body>" + "</html>");
//                tv_overed_webview.getSettings().setDefaultTextEncodingName("UTF -8");
//                tv_overed_webview.loadData(html.toString(), "text/html; charset=UTF-8", null);
//                tv_overed_webview.setVerticalScrollBarEnabled(false);
//                mOveredTips.setVisibility(View.VISIBLE);
//                mTvOveredTips.setText("本场已结束");
//                headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                //右上角结束大邮戳  没这玩意
//                //此处加载本场结束的数据
//            }
//            txt_next_yg.setVisibility(mRecommendModel.getInfo().getTimes() > 0 ? View.VISIBLE : View.GONE);
//            txt_next_yg.setText("第" + mRecommendModel.getInfo().getTimes() + "期");
//        }
//        ViewTreeObserver vto = layout_out_head.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                layout_out_head.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                head_height = layout_out_head.getHeight();
//                //layout_out_head.getWidth();
//            }
//        });
//    }
//
//    /**
//     * 价格高 价格低 同时有
//     */
//    private boolean hasPriceValue(List<RecommendModel.InfoEntity.SortMenusBean> sortMenus) {
//      /*  {
//            "Title": "价格高",
//                "Value": 4
//        },
//        {
//            "Title": "价格低",
//                "Value": 5
//        },*/
//        boolean flag = false;
//        List<Integer> values = new ArrayList<>();
//        for (int i = 0; i < sortMenus.size(); i++) {
//            int value = sortMenus.get(i).getValue();
//            if (value == 5 || value == 4) {
//                values.add(value);
//            }
//        }
//        if (values.size() == 2) {
//            flag = true;
//        }
//        return flag;
//    }
//
//    TextView[] mTvs, mTvs2;
//    int mCrrentPos;
//    Drawable nav_choose_sel;
//
//    //新款排序
//    private void initbottommenu() {
//        currentMenuID = mRecommendModel.getInfo().getCurrentMenuID();
//        final List<RecommendModel.InfoEntity.SortMenusBean> sortMenus1 = mRecommendModel.getInfo().getSortMenus();
//        boolean hasValue;
//        if (sortMenus1 != null && sortMenus1.size() > 0) {
//            ll_bottomx.removeAllViews();
//            ll_bottomx2.removeAllViews();
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//            LinearLayout.LayoutParams vparams = new LinearLayout.LayoutParams(DisplayUtil.dip2px(mContext, 1), LinearLayout.LayoutParams.MATCH_PARENT);
//            vparams.topMargin = DisplayUtil.dip2px(mContext, 10);
//            vparams.bottomMargin = DisplayUtil.dip2px(mContext, 10);
//            int drawpadding = DisplayUtil.dip2px(mContext, -8);
//            int paddingRight = DisplayUtil.dip2px(mContext, 8);
//            final Drawable nav_nomal = getResources().getDrawable(R.drawable.todefaut);
//            nav_nomal.setBounds(0, 0, nav_nomal.getMinimumWidth(), nav_nomal.getMinimumHeight());
//            final Drawable nav_up = getResources().getDrawable(R.drawable.toup);
//            nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
//            final Drawable nav_dowm = getResources().getDrawable(R.drawable.todown);
//            nav_dowm.setBounds(0, 0, nav_dowm.getMinimumWidth(), nav_dowm.getMinimumHeight());
//            final Drawable nav_choose = getResources().getDrawable(R.drawable.screen_choose);
//            nav_choose.setBounds(0, 0, nav_choose.getMinimumWidth(), nav_choose.getMinimumHeight());
//            nav_choose_sel = getResources().getDrawable(R.drawable.screen_choose_click);
//            nav_choose_sel.setBounds(0, 0, nav_choose_sel.getMinimumWidth(), nav_choose_sel.getMinimumHeight());
//            ll_bottomx.setVisibility(View.VISIBLE);
//            ll_bottomx2.setVisibility(View.VISIBLE);
//            final List<RecommendModel.InfoEntity.SortMenusBean> sortMenus = new ArrayList<>();
//            hasValue = hasPriceValue(sortMenus1);
//            if (hasValue) {
//                for (RecommendModel.InfoEntity.SortMenusBean sortMenusBean : sortMenus1) {
//                    int value = sortMenusBean.getValue();
//                    if (value != 5) {
//                        if (currentMenuID == 4 || currentMenuID == 5) {
//                            if (value == 4) {
//                                sortMenusBean.setValue(currentMenuID);
//                                sortMenusBean.setTitle("价格");
//                            }
//                        }
//                        if (value == 4) {
//                            sortMenusBean.setTitle("价格");
//                        }
//                        sortMenus.add(sortMenusBean);
//                    }
//                }
//            } else {
//                sortMenus.addAll(sortMenus1);
//            }
//            mTvs = new TextView[sortMenus.size()];
//            mTvs2 = new TextView[sortMenus.size()];
//            for (int i = 0; i < sortMenus.size(); i++) {
//                TextView tv = new TextView(mContext);
//                View view = new View(mContext);
//                view.setLayoutParams(vparams);
//                view.setBackgroundColor(Color.parseColor("#D9D9D9"));
//                int value = sortMenus.get(i).getValue();
//                tv.setGravity(Gravity.CENTER);
//                tv.setTextSize(16);
//                //tv.setPadding(30, 20, 30, 20);
//                tv.setLayoutParams(params);
//                tv.setText(sortMenus.get(i).getTitle());
//
//                TextView tv2 = new TextView(mContext);
//                View view2 = new View(mContext);
//                view2.setLayoutParams(vparams);
//                view2.setBackgroundColor(Color.parseColor("#D9D9D9"));
//                tv2.setGravity(Gravity.CENTER);
//                tv2.setTextSize(16);
//                tv2.setLayoutParams(params);
//                tv2.setText(sortMenus.get(i).getTitle());
//
//                if (value == 4 || value == 5 || value == 20) {
//                    tv.setPadding(0, 0, paddingRight, 0);
//                    tv.setCompoundDrawablePadding(drawpadding);
//                    tv2.setPadding(0, 0, paddingRight, 0);
//                    tv2.setCompoundDrawablePadding(drawpadding);
//                }
//                if (value == currentMenuID) {
//                    if (value == 5) {
//                        tv.setCompoundDrawables(null, null, nav_up, null);
//                        tv2.setCompoundDrawables(null, null, nav_up, null);
//                    } else if (value == 4) {
//                        tv.setCompoundDrawables(null, null, nav_dowm, null);
//                        tv2.setCompoundDrawables(null, null, nav_dowm, null);
//                    } else if (value == 20) {
//                        tv.setCompoundDrawables(null, null, nav_choose_sel, null);
//                        tv2.setCompoundDrawables(null, null, nav_choose_sel, null);
//                    }
//                    //tv.setCompoundDrawables(null, null, nav_up1, null);
//                    tv.setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    tv2.setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                } else {
//                    if (value == 5 || value == 4) {
//                        tv.setCompoundDrawables(null, null, nav_nomal, null);
//                        tv2.setCompoundDrawables(null, null, nav_nomal, null);
//                    } else if (value == 20) {
//                        tv.setCompoundDrawables(null, null, nav_choose, null);
//                        tv2.setCompoundDrawables(null, null, nav_choose, null);
//                    }
//                    tv.setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                    tv2.setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                    //tv.setCompoundDrawables(null, null, nav_up, null);
//                }
//                mTvs[i] = tv;
//                mTvs2[i] = tv2;
//                ll_bottomx.addView(tv);
//                ll_bottomx2.addView(tv2);
//                if (i < sortMenus.size() - 1) {
//                    ll_bottomx.addView(view);
//                    ll_bottomx2.addView(view2);
//                }
//                final int pos = i;
//                tv.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCrrentPos = pos;
//                        setItemOnclick(mTvs, pos, sortMenus, nav_nomal, nav_choose);
//                        setItemOnclick(mTvs2, pos, sortMenus, nav_nomal, nav_choose);
//                    }
//                });
//                tv2.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mCrrentPos = pos;
//                        setItemOnclick(mTvs, pos, sortMenus, nav_nomal, nav_choose);
//                        setItemOnclick(mTvs2, pos, sortMenus, nav_nomal, nav_choose);
//                    }
//                });
//
//            }
//
//
//        } else {
//            //  ll_bottomx.setVisibility(View.GONE);
//        }
//    }
//
//    private void setItemOnclick(TextView[] mTvs, int pos, List<RecommendModel.InfoEntity.SortMenusBean> sortMenus, Drawable nav_nomal, Drawable nav_choose) {
//        for (int j = 0; j < mTvs.length; j++) {
//
//            if (j == pos) {
//                // mTvs[j].setCompoundDrawables(null, null, nav_up1, null);
//                int value = sortMenus.get(pos).getValue();
//                if (value == 20) {
//                    mDrawerLayout.openDrawer(drawer_content);
//                    if (first_Post_Panels) {
//                        getSearchPanel();
//                    }
//                    //  scrollToPosition();
//                } else if (value == 4) {
//                    sortBy = 5;
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    loadData(true, true);
//                } else if (value == 5) {
//                    sortBy = 4;
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    loadData(true, true);
//                } else {
//                    sortBy = value;
//                    mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    loadData(true, true);
//                }
//            } else {
//                int value = sortMenus.get(pos).getValue();
//                if (value != 20) {
//                    mTvs[j].setTextColor(getResources().getColor(R.color.bottom_item_txt_normal));
//                    int sValue = sortMenus.get(j).getValue();
//                    if (sValue == 5) {
//                        mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
//                    } else if (sValue == 4) {
//                        mTvs[j].setCompoundDrawables(null, null, nav_nomal, null);
//                    } else if (sValue == 20) {
//                        mTvs[j].setCompoundDrawables(null, null, nav_choose, null);
//                    }
//                }
//            }
//        }
//    }
//
//    //筛选更多接口
//    private void getSearchPanel() {
//        try {
//            JSONObject jsonobject = new JSONObject();
//            jsonobject.put("ID", QID);
//            jsonobject.put("Keyword", "");
//            jsonobject.put("Values", "");
//            QuickSaleApi.getSearchPanel(mAppContext, mRequestHelper, this, IDsConstant.AREAID_LIST + "", jsonobject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void initViews() {
//        initDrawView();
//        top_bottomx = $(R.id.top_bottomx);
//        mVScrollToTop = $(R.id.iv_scroll_to_top);
//        tv_draw_layout_ok = $(R.id.tv_draw_layout_ok);
//        tv_draw_layout_ok.setOnClickListener(this);
//        tv_draw_layout_clear = $(R.id.tv_draw_layout_clear);
//        tv_draw_layout_clear.setOnClickListener(this);
//        mVScrollToTop.setOnClickListener(this);
//        ll_bottom = $(R.id.ll_bottom);
//        ll_bottomx2 = $(R.id.ll_bottomx);
//        //hs_bottomx = $(R.id.hs_bottomx);
//        mNextActivitysView = $(R.id.ll_next_activity);
//        mTvNextActivity = $(R.id.ll_next_activity_time);
//        $(R.id.btn_next_activity).setOnClickListener(this);
//        tvDealSort = $(R.id.tv_deal);
//        tvDealSort.setOnClickListener(this);
//        tvShopCat = $(R.id.tv_shopcat);
//        tvShopCat.setOnClickListener(this);
//        tvCollectSort = $(R.id.tv_collect);
//        tvCollectSort.setOnClickListener(this);
//        tvMustDealSort = $(R.id.tv_mustdeal);
//        tvMustDealSort.setOnClickListener(this);
//        if (isOvered) {
//            tvMustDealSort.setVisibility(View.INVISIBLE);
//        }
//
//        View headView = initHeadView();
//        initFloatHeadView();
//        mRefreshLayout = $(R.id.refresh_layout);
//        mRefreshLayout.setCallBack(this);
//        mEmptyView = $(R.id.empty_view);
//        mEmptyView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRefreshLayout.manualRefresh();
//            }
//        });
//        mRecyclerView = $(R.id.recycler_view);
//        final GridLayoutManager gridManager = new GridLayoutManager(mActivity, 2);
//        mRecyclerView.setLayoutManager(gridManager);
//        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//
//        mNewItemAdapter = new PinHuoDetailAdapter();
//        mNewItemBookends = new Bookends(mNewItemAdapter, gridManager);
//        mNewItemBookends.addHeader(headView);
//        mRecyclerView.setAdapter(mNewItemBookends);
//        mNewItemAdapter.setListener(new Listener() {
//            @Override
//            public void onItemClick(ShopItemListModel item) {
//                Intent intent = new Intent(mActivity, ItemDetailsActivity.class);
//                intent.putExtra(ItemDetailsActivity.EXTRA_ID, item.getID());
//                intent.putExtra(ItemDetailsActivity.EXTRA_PIN_HUO_ID, mRecommendModel.getInfo().getID());
//                startActivity(intent);
//            }
//        });
//
//        View footerView = LayoutInflater.from(mAppContext).inflate(R.layout.layout_pin_huo_detail_passitem, null);
//        mLoadMorePB = (ProgressBar) footerView.findViewById(R.id.progressbar);
//        mLoadMoreTxt = (TextView) footerView.findViewById(R.id.load_more_txt);
//        mNewItemBookends.addFooter(footerView);
//        mRecyclerView.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                try {
//                    // Log.e("yu", "ScollY=" + getScollYDistance() + "head_height" + head_height);
//                    int pos = gridManager.findFirstVisibleItemPosition();
//                    if (pos >= 1) {
//                        JCVideoPlayer.releaseAllVideos();
//                    }
//                    if (getScollYDistance() > 0 && getScollYDistance() >= head_height && head_height > 0) {
//                        if (mRecommendModel.getInfo().isStart() &&
//                                mRecommendModel.getInfo().getActivityType().equals("拼货") &&
//                                mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
//                            if (countdownDuration > 0) {
//                                mFloatHeadView.setVisibility(View.VISIBLE);
//                            } else {
//                                mFloatHeadView.setVisibility(View.GONE);
//                            }
//
//                        } else {
//                            mFloatHeadView.setVisibility(View.GONE);
//                        }
//                        if (ll_bottomx.getVisibility() == View.VISIBLE) {
//                            top_bottomx.setVisibility(View.VISIBLE);
//                        } else {
//                            top_bottomx.setVisibility(View.GONE);
//                        }
//                    } else {
//                        if (pos >= 1) {
//                            mFloatHeadView.setVisibility(View.VISIBLE);
//                            if (ll_bottomx.getVisibility() == View.VISIBLE) {
//                                top_bottomx.setVisibility(View.VISIBLE);
//                            } else {
//                                top_bottomx.setVisibility(View.GONE);
//                            }
//                        } else {
//                            mFloatHeadView.setVisibility(View.GONE);
//                            top_bottomx.setVisibility(View.GONE);
//                        }
//                    }
////                    if (pos > 1) {
////                        if (mRecommendModel.getInfo().isStart() &&
////                                mRecommendModel.getInfo().getActivityType().equals("拼货") &&
////                                mRecommendModel.getInfo().getOpenStatu().equals("开拼中")) {
////                            if (countdownDuration > 0) {
////                                mFloatHeadView.setVisibility(View.VISIBLE);
////                            } else {
////                                mFloatHeadView.setVisibility(View.GONE);
////                            }
////
////                        } else {
////                            mFloatHeadView.setVisibility(View.GONE);
////                        }
////                    } else {
////                        mFloatHeadView.setVisibility(View.GONE);
////                    }
//                    if (mLastPos != pos) {
//                        mLastPos = pos;
//                    }
//                    if (pos > 5 && mVScrollToTop.getVisibility() != View.VISIBLE) {
//                        mVScrollToTop.setVisibility(View.VISIBLE);
//                    } else if (pos <= 5 && mVScrollToTop.getVisibility() != View.GONE) {
//                        mVScrollToTop.setVisibility(View.GONE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
////                mVScrollToTop.setVisibility(pos > 5 ? View.VISIBLE : View.GONE);
//            }
//        });
//
//    }
//
//    /**
//     * 让RecyclerView滚动到指定位置
//     */
//    private void scrollToPosition() {
//        if (recycler_drawer != null)
//            if (recycler_drawer.getLayoutManager() != null && lastPosition >= 0) {
//                recycler_drawer.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        linearLayoutManager.scrollToPositionWithOffset(lastPosition, lastOffset);
//                        recycler_drawer.setVisibility(View.VISIBLE);
//                    }
//                }, 100);
//            }
//    }
//
//    /**
//     * 记录RecyclerView当前位置
//     */
//    private void getPositionAndOffset() {
//        //获取可视的第一个view
//        View topView = linearLayoutManager.getChildAt(0);
//        if (topView != null) {
//            //获取与该view的顶部的偏移量
//            lastOffset = topView.getTop();
//            //得到该View的数组位置
//            lastPosition = linearLayoutManager.getPosition(topView);
//        }
//    }
//
//    private void initDrawView() {
//        mDrawerLayout = $(R.id.drawer_layout);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        recycler_drawer = $(R.id.recycler_drawer);
//        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
//        recycler_drawer.setLayoutManager(linearLayoutManager);
//        sortFilterAdpater = new SortFilterAdpater(getActivity());
//        sortFilterAdpater.setDrawRecyclerView(recycler_drawer);
//        recycler_drawer.setAdapter(sortFilterAdpater);
//        recycler_drawer.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                // getPositionAndOffset();
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (recyclerView.getLayoutManager() != null) {
//                    getPositionAndOffset();
//                }
//            }
//        });
//
//        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//            }
//
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
//                scrollToPosition();
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                FunctionHelper.hideSoftInput(getActivity());
//                if (recycler_drawer != null)
//                    recycler_drawer.setVisibility(View.INVISIBLE);
//                //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//            }
//        });
//        drawer_content = $(R.id.drawer_content);
//        drawer_content.setClickable(true);
//    }
//
//    public int getScollYDistance() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//        int position = layoutManager.findFirstVisibleItemPosition();
//        View firstVisiableChildView = layoutManager.findViewByPosition(position);
//        int itemHeight = firstVisiableChildView.getHeight();
//        return (position) * itemHeight - firstVisiableChildView.getTop();
//    }
//
//    private View headView;
//
//    @NonNull
//    private View initHeadView() {
//        headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_pin_huo_list_head_view, null);
//        headView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = mRecommendModel.getInfo().getUrl();
//                if (url.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (url.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                    intent.putExtra("name", "拼货预告");
//                    intent.putExtra("url", url);
//                    mContext.startActivity(intent);
//                }
//            }
//        });
//        layout_out_head = headView.findViewById(R.id.layout_out_head);
//        video_player = (ItemJCVideoPlayerStandard) headView.findViewById(R.id.video_player);
//        ll_bottomx = (LinearLayout) headView.findViewById(R.id.ll_bottomx);
//        layout_need_hide = headView.findViewById(R.id.layout_need_hide);
//        tv_shop_intro = (TextView) headView.findViewById(R.id.tv_shop_intro);
//        tv_stall_name = (TextView) headView.findViewById(R.id.tv_stall_name);
//        mIvCover = (ImageView) headView.findViewById(R.id.iv_cover);
//        iv_cover_yg = (ImageView) headView.findViewById(R.id.iv_cover_yg);
//        iv_cover_ended = (ImageView) headView.findViewById(R.id.iv_cover_ended);
//        txt_next_yg = (TextView) headView.findViewById(R.id.txt_next_yg);
//        mIvNewItemIcon = (ImageView) headView.findViewById(R.id.iv_newitem_icon);
////        iv_cover_pining = (ImageView) headView.findViewById(R.id.iv_cover_pining);
//        mOveredTips = (View) headView.findViewById(R.id.iv_over_tips);
//        lladd = (LinearLayout) headView.findViewById(R.id.lladd);
//        tv_overed_webview = (WebView) headView.findViewById(R.id.tv_overed_webview);
//
//        mTvShopDesc = (TextView) headView.findViewById(R.id.tv_shop_desc);
//        mTvShopName = (TextView) headView.findViewById(R.id.tv_shop_name);
//        shopIcon = (CircleImageView) headView.findViewById(R.id.tv_shop_icon);
//        mTvHH = (TextView) headView.findViewById(R.id.tv_hh);
//        mTvH = (TextView) headView.findViewById(R.id.tv_h);
//        mTvMM = (TextView) headView.findViewById(R.id.tv_mm);
//        mTvM = (TextView) headView.findViewById(R.id.tv_m);
//        mTvSS = (TextView) headView.findViewById(R.id.tv_ss);
//        mTvS = (TextView) headView.findViewById(R.id.tv_s);
//        mTvF = (TextView) headView.findViewById(R.id.tv_f);
//        mTvDay = (TextView) headView.findViewById(R.id.tv_day);
//        mTvDayDesc = (TextView) headView.findViewById(R.id.tv_day_desc);
////        mTvOveredTips = (TextView) headView.findViewById(R.id.tv_overed_tips);
//        mTvOveredTips = (TextView) lladd.findViewById(R.id.tv_overed_tips);
//        mTvNextTips = (WebView) lladd.findViewById(R.id.tv_next_tips);
//        btn_focus = (TextView) headView.findViewById(R.id.btn_focus);
//        btn_focus.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int notice;
//                notice = R.string.fucous_notice;
////                if (!isFocus) {
////                    notice = R.string.fucous_notice;
////                } else {
////                    notice = R.string.cancel_fucous_notice;
////                }
//                QuickSaleApi.saveFocus(mContext, mRequestHelper, new HttpRequestListener() {
//                    @Override
//                    public void onRequestStart(String method) {
//                        if (isFocus) {
//                            mLoadingDialog.start("取消关注中...");
//                        } else {
//                            mLoadingDialog.start("关注中...");
//                        }
//                    }
//
//                    @Override
//                    public void onRequestSuccess(String method, Object object) {
//                        mLoadingDialog.stop();
//                        EventBus.getDefault().post(BusEvent.getEvent(EventBusId.REFRESH_COMPLETEd1));
//                        if (isFocus) {
//                            isFocus = false;
//                            btn_focus.setText("关注本团");
//                            btn_focus.setTextColor(getResources().getColor(R.color.text_red));
//                            btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
//                            ViewHub.showLongToast(mContext, "已取消关注");
//                        } else {
//                            isFocus = true;
//                            btn_focus.setText("取消关注");
//                            btn_focus.setTextColor(getResources().getColor(R.color.gray_92));
//                            btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
//                            ViewHub.showLongToast(mContext, "已关注");
//                        }
//                    }
//
//                    @Override
//                    public void onRequestFail(String method, int statusCode, String msg) {
//                        mLoadingDialog.stop();
//                        ViewHub.showShortToast(mContext, msg);
//                    }
//
//                    @Override
//                    public void onRequestExp(String method, String msg, ResultData data) {
//                        mLoadingDialog.stop();
//                        ViewHub.showShortToast(mContext, msg);
//                    }
//                }, ID);
//                String use_id = SpManager.getUserId(mActivity) + "";
//                FocusSave focusSave = new FocusSave(mContext, use_id);
//                if (!focusSave.getFocus(FIRST_FOCUS_DETAI_PRE + use_id)) {
//                    focusSave.setFocus(FIRST_FOCUS_DETAI_PRE + use_id, true);
//                    DomDialog dialog = new DomDialog(mActivity);
//                    dialog.setTitle("提示").setMessage(notice).setPositive("关闭", new DomDialog.PopDialogListener() {
//                        @Override
//                        public void onPopDialogButtonClick(int which) {
//
//                        }
//                    }).show();
//                }
//
//            }
//        });
////        if (mPinHuoModel.getActivityType().equals("拼货")) {
////            lladd.setVisibility(View.VISIBLE);
////            if (mPinHuoModel.getOpenStatu() != null && mPinHuoModel.getOpenStatu().getStatu().equals("开拼中")) {
////                mIvNewItemIcon.setVisibility(View.VISIBLE);
////            } else {
////                mIvNewItemIcon.setVisibility(View.GONE);
////            }
////        } else {
////
////            lladd.setVisibility(View.GONE);
////            mIvNewItemIcon.setVisibility(View.GONE);
////        }
//
//        return headView;
//    }
//
//    private void initFloatHeadView() {
//        mFloatHeadView = mContentView.findViewById(R.id.layout_float_head_view);
//        mTvFHH = (TextView) mFloatHeadView.findViewById(R.id.tv_hh);
//        mTvFH = (TextView) mFloatHeadView.findViewById(R.id.tv_h);
//        mTvFMM = (TextView) mFloatHeadView.findViewById(R.id.tv_mm);
//        mTvFM = (TextView) mFloatHeadView.findViewById(R.id.tv_m);
//        mTvFSS = (TextView) mFloatHeadView.findViewById(R.id.tv_ss);
//        mTvFS = (TextView) mFloatHeadView.findViewById(R.id.tv_s);
//        mTvFF = (TextView) mFloatHeadView.findViewById(R.id.tv_f);
//        mTvFDay = (TextView) mFloatHeadView.findViewById(R.id.tv_day);
//        mTvFDayDesc = (TextView) mFloatHeadView.findViewById(R.id.tv_day_desc);
//        mTvfOveredTips = (TextView) mFloatHeadView.findViewById(R.id.tv_overed_tips);
//        if (isOvered) {
//            mFloatHeadView.findViewById(R.id.tv_overed_tips).setVisibility(View.VISIBLE);
//            mFloatHeadView.findViewById(R.id.ll_top1).setVisibility(View.GONE);
//        } else {
//            mFloatHeadView.findViewById(R.id.tv_overed_tips).setVisibility(View.GONE);
//            mFloatHeadView.findViewById(R.id.ll_top1).setVisibility(View.VISIBLE);
//        }
//        mFloatHeadView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = mPinHuoModel.getUrl();
//                if (url.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (url.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                    intent.putExtra("name", "拼货预告");
//                    intent.putExtra("url", url);
//                    mContext.startActivity(intent);
//                }
//            }
//        });
//    }
//
//    private void loadData(boolean isRefresh, boolean showLoading) {
//        mRefreshLayout.setCanLoadMore(true);
//        if (mPinHuoModel != null)
//            QID = mPinHuoModel.ID > 0 ? mPinHuoModel.ID : mPinHuoModel.QsID;
//        if (isTasking) {
//            return;
//        } else {
//            if (mLoadingDialog == null && mContext != null)
//                mLoadingDialog = new LoadingDialog(mContext);
//
//            mNewItemBookends.setShowFooter(true);
//            mLoadMorePB.setVisibility(View.VISIBLE);
//            mLoadMoreTxt.setText("正在加载更多");
//            //showEmptyView(false);
//            mIsRefresh = isRefresh;
//            isTasking = true;
//            PAGE_INDEX = mIsRefresh ? 1 : ++PAGE_INDEX;
//            if (getActivity() != null) {
//                if (showLoading) {
//                    try {
//                        mLoadingDialog.show();
//                    } catch (Exception e) {
//                        e.toString();
//                    }
//                }
//            }
//            QuickSaleApi.getRecommendShopItemsx(mAppContext,
//                    mRequestHelper,
//                    this,
//                    mPinHuoModel.ID > 0 ? mPinHuoModel.ID : mPinHuoModel.QsID,//服务器一会用ID,一会用QSID,不要问我为什么,公用页面就是这么蛋疼
//                    PAGE_INDEX,
//                    PAGE_SIZE,
//                    mPinHuoModel.keyword,
//                    sortBy,
//                    mShopCatId,
//                    displayMode, filterValues);
//        }
//
//
//    }
//
//
//    @Override
//    public void onRefresh() {
//        //Log.v(TAG,"start onresume");
//        if (mNeedToClearKeyword) {//第一次之后需要清除搜索关键字
//            mPinHuoModel.keyword = "";
//        }
//        mNeedToClearKeyword = true;
//        if (n == 1) {
//            if (mSortIndex == Const.SortIndex.DefaultDesc) {
//                mSortIndex = Const.SortIndex.Default;
//            }
//        }
//        mNewItemAdapter.clear();
//        displayMode = 0;
//        loadData(true, false);
//        // displayMode = 1;
//
//        //new LoadGoodsTask(getActivity(),carCountTv).execute();
//
//    }
//
//    @Override
//    public void onLoadMore() {
//        // mRefreshLayout.setCanLoadMore(true);
//        loadData(false, false);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        if (!isOvered && mRecommendModel != null) {
//            long countdownDuration = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
//            SpManager.setQuickSellEndTime(mAppContext, mRecommendModel.getInfo().getEndMillis());
//            mCountDownTimer = new MyCountDownTimer(countdownDuration);
//            mCountDownTimer.start();
//        }
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mCountDownTimer != null) {
//            mCountDownTimer.cancel();
//            mCountDownTimer = null;
//        }
//    }
//
//    @Override
//    public void onRequestStart(String method) {
//        super.onRequestStart(method);
//        switch (method) {
//            case RECOMMEND_SHOP_ITEMS:
//                showDialog(mLoadingDialog, "正在加载商品");
//                break;
//        }
//
//    }
//
//    public void showDialog(LoadingDialog mLoadingDialog, String mess) {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.setMessage(mess);
//            mLoadingDialog.show();
//        }
//    }
//
//    public void closeDialog(LoadingDialog mLoadingDialog) {
//        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
//            mLoadingDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void onRequestSuccess(String method, Object object) {
//        super.onRequestSuccess(method, object);
//        switch (method) {
//            case RECOMMEND_SHOP_ITEMS:
//                closeDialog(mLoadingDialog);
//                onDataLoaded(object);
//                break;
//            case RequestMethod.IteMizeMethod.Get_Search_Panel:
//                // Log.d("yu", object.toString());
//                SearchPanelBean xsearchPanelBean = (SearchPanelBean) object;
//                if (ListUtils.isEmpty(Panels)) {
//                    if (xsearchPanelBean != null) {
//                        if (!ListUtils.isEmpty(xsearchPanelBean.getPanels())) {
//                            first_Post_Panels = false;
//                            searchPanelBean = xsearchPanelBean;
//                            Panels.addAll(xsearchPanelBean.getPanels());
//                            if (sortFilterAdpater != null) {
//                                sortFilterAdpater.setPanels(searchPanelBean);
//                                sortFilterAdpater.notifyDataSetChanged();
//                            }
//                        }
//                    }
//
//
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onRequestFail(String method, int statusCode, String msg) {
//        switch (method) {
//            case RECOMMEND_SHOP_ITEMS:
//                closeDialog(mLoadingDialog);
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                break;
//        }
//
//    }
//
//    @Override
//    public void onRequestExp(String method, String msg, ResultData data) {
//        switch (method) {
//            case RECOMMEND_SHOP_ITEMS:
//                closeDialog(mLoadingDialog);
//                loadFinished();
//                onLoadPinAndForecastFailed();
//                break;
//        }
//    }
//
//    public void onLoadPinAndForecastFailed() {
//        loadFinished();
//        ViewHub.showShortToast(mContext, "加载失败，请稍候再试");
//    }
//
//    private void loadFinished() {
//        if (mLoadingDialog != null) {
//            mLoadingDialog.stop();
//        }
//        isTasking = false;
//        mRefreshLayout.completeRefresh();
//        mRefreshLayout.completeLoadMore();
//    }
//
//    public void getFocusStat() {
//        List<String> s = new ArrayList<>();
//        ID = mRecommendModel.getInfo().getShopID();
//        s.add("" + ID);
//        QuickSaleApi.getFocusStatList(mContext, mRequestHelper, new HttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
////                mLoadingDialog.start("读取关注数据中");
//            }
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
////                mLoadingDialog.stop();
//                // btn_focus.setVisibility(View.VISIBLE);
//                try {
//                    List<Integer> shopids = GsonHelper.jsonToObject(object.toString(),
//                            new TypeToken<List<Integer>>() {
//                            });
//                    if (shopids.indexOf(ID) > -1) {
//                        isFocus = true;
//                        btn_focus.setText("取消关注");
//                        btn_focus.setTextColor(getResources().getColor(R.color.gray_92));
//                        btn_focus.setBackgroundResource(R.drawable.bg_rectangle_grayx);
//                    } else {
//                        isFocus = false;
//                        btn_focus.setText("关注本团");
//                        btn_focus.setTextColor(getResources().getColor(R.color.text_red));
//                        btn_focus.setBackgroundResource(R.drawable.bg_rectangle_red1);
//                    }
//                } catch (Exception ex) {
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
////                mLoadingDialog.stop();
//                ViewHub.showShortToast(mContext, msg);
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
////                mLoadingDialog.stop();
//                ViewHub.showShortToast(mContext, msg);
//            }
//        }, s);
//    }
//
//    private void onDataLoaded(Object obj) {
//
//        try {
//            //mRefreshLayout.setCanLoadMore(true);
//            loadFinished();
//            mRecommendModel = (RecommendModel) obj;
//            bindData();
//            getFocusStat();
//            if (mItemShopCategories == null || mItemShopCategories.size() == 0) {
//                GetShopCategoriesTask gcbt = new GetShopCategoriesTask();
//                gcbt.execute();
//            }
////        mRecommendModel.PassItems = mRecommendModel.NewItems;
////        mRecommendModel.NewItems = new ArrayList<>();
//            if (mRecommendModel.getInfo().getEndMillis() < System.currentTimeMillis()) {//今天的场次已结束，读取下一场的
//                //                initActivityTime(mRecommendModel.getNextStartMillis(), mRecommendModel.getNextEndMillis());
//                SpManager.setQuickSellEndTime(mAppContext, 0);
//
//            } else {
//                //                initActivityTime(mRecommendModel.getStartMillis(), mRecommendModel.getEndMillis());
//                SpManager.setQuickSellStartTime(mAppContext, mRecommendModel.getInfo().getStartMillis());
//                SpManager.setQuickSellEndTime(mAppContext, mRecommendModel.getInfo().getEndMillis());
//            }
//            Bookends.part2title = mRecommendModel.getInfo().getPart2Title();
//            if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                if (lladd != null)
//                    lladd.setVisibility(View.GONE);
//            }
//            // displayMode 0 旧款新款 1新 2 旧
//            if (mIsRefresh) {
//                initbottommenu();
//                newCount = 0;
//                oldeCount = 0;
//                if (mRecommendModel.getInfo().getChengTuanCount() > 0) {
//                    //有些接口成团数量写在头部,有些写在list数据里,不要问我为什么
//                    mNewItemAdapter.setChengTuanCount(mRecommendModel.getInfo().getChengTuanCount());
//                }
//                //            if (mRecommendModel.getEndMillis() < System.currentTimeMillis()) {//今天的场次已结束，读取下一场的
//                ////                initActivityTime(mRecommendModel.getNextStartMillis(), mRecommendModel.getNextEndMillis());
//                //                SpManager.setQuickSellEndTime(mAppContext,0);
//                //
//                //            } else {
//                ////                initActivityTime(mRecommendModel.getStartMillis(), mRecommendModel.getEndMillis());
//                //                SpManager.setQuickSellStartTime(mAppContext, mRecommendModel.getStartMillis());
//                //                SpManager.setQuickSellEndTime(mAppContext, mRecommendModel.getInfo().getEndMillis());
//                //            }
//                mNewItemAdapter.setData(mRecommendModel.NewItems);
//                PassItems = mRecommendModel.PassItems;
//                firstPassItems = PassItems;
//                if (ListUtils.isEmpty(mRecommendModel.PassItems) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    Bookends.passPosindex = -100;
//                    showEmptyView(true);
//                    //hs_bottomx.setVisibility(View.GONE);
//                    //ll_bottomx.setVisibility(View.GONE);
//                    // ll_bottom.setVisibility(View.GONE);
//                    //mIvNewItemIcon.setVisibility(View.GONE);
//                } else if (!ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    //ll_bottom.setVisibility(View.VISIBLE);
//                    //ll_bottomx.setVisibility(View.VISIBLE);
//                    // hs_bottomx.setVisibility(View.VISIBLE);
//                    if (mRecommendModel.NewItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        newCount = mRecommendModel.NewItems.size();
//                        if (!ListUtils.isEmpty(firstPassItems)) {
//                            if (firstPassItems.size() + newCount < 20) {
//                                Bookends.passPosindex = newCount;
//                                mNewItemAdapter.setPassItemPosition1();
//                                mNewItemAdapter.addPassItem(firstPassItems);
//                                mLoadMorePB.setVisibility(View.GONE);
//                                mLoadMoreTxt.setText("没有更多款式了");
//                                mRefreshLayout.setCanLoadMore(false);
//                            } else {
//                                Bookends.passPosindex = newCount;
//                                mNewItemAdapter.setPassItemPosition1();
//                                mNewItemAdapter.addPassItem(firstPassItems);
//                                displayMode = 2;
//                            }
//
//                        } else {
//                            //没有旧款加载完了
//                            Bookends.passPosindex = -100;
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                            mRefreshLayout.setCanLoadMore(false);
//                        }
//                    } else {
//                        //加载新款
//                        Bookends.passPosindex = -100;
//                        newCount = mRecommendModel.NewItems.size();
//                        displayMode = 1;
//                    }
//                } else {
//                    //hs_bottomx.setVisibility(View.GONE);
//                    // ll_bottomx.setVisibility(View.GONE);
//                    // mIvNewItemIcon.setVisibility(View.GONE);
//                    //ll_bottom.setVisibility(View.GONE);
//                    if (!ListUtils.isEmpty(firstPassItems)) {
//                        if (firstPassItems.size() < 20) {
//                            //加载完
//                            Bookends.passPosindex = 0;
//                            mNewItemAdapter.setPassItemPosition1();
//                            mNewItemAdapter.addPassItem(firstPassItems);
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                            mRefreshLayout.setCanLoadMore(false);
//                        } else {
//                            Bookends.passPosindex = 0;
//                            mNewItemAdapter.setPassItemPosition1();
//                            mNewItemAdapter.addPassItem(firstPassItems);
//                            displayMode = 2;
//                        }
//
//                    } else {
//                        Bookends.passPosindex = -100;
//                    }
//                }
//                n = 1;
//                mNewItemAdapter.notifyDataSetChanged();
//
//            } else {
//                //加载更多
//                //继续加载新款
//                if ((displayMode == 1) && !ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    if (mRecommendModel.NewItems.size() < 20) {
//                        //新款加载完成去加载旧款
//                        mNewItemAdapter.addDataToTail(mRecommendModel.NewItems);
//                        newCount = mRecommendModel.NewItems.size() + newCount;
//                        if (!ListUtils.isEmpty(firstPassItems)) {
//                            //加载第一页旧款
//                            Bookends.passPosindex = newCount;
//                            mNewItemAdapter.setPassItemPosition1();
//                            mNewItemAdapter.addPassItem(firstPassItems);
//                            displayMode = 2;
//                            PAGE_INDEX = 1;
//                        } else {
//                            //新款加载完了没有旧款
//                            Bookends.passPosindex = -100;
//                            mLoadMorePB.setVisibility(View.GONE);
//                            mLoadMoreTxt.setText("没有更多款式了");
//                            mRefreshLayout.setCanLoadMore(false);
//                        }
//                        //loadData(false, false);
//                    } else {
//                        //加载新款
//                        newCount = mRecommendModel.NewItems.size() + newCount;
//                        mNewItemAdapter.addDataToTail(mRecommendModel.NewItems);
//                        displayMode = 1;
//                        //loadData(false, false);
//                    }
//                } else if ((displayMode == 1) && ListUtils.isEmpty(mRecommendModel.NewItems)) {
//                    //新款没了加载第一页旧款
//                    if (!ListUtils.isEmpty(firstPassItems)) {
//                        Bookends.passPosindex = newCount;
//                        mNewItemAdapter.setPassItemPosition1();
//                        mNewItemAdapter.addPassItem(firstPassItems);
//                        displayMode = 2;
//                        PAGE_INDEX = 1;
//                        //loadData(false, false);
//                    } else {
//                        //加载完了
//                        Bookends.passPosindex = -100;
//                        mLoadMorePB.setVisibility(View.GONE);
//                        mLoadMoreTxt.setText("没有更多款式了");
//                        mRefreshLayout.setCanLoadMore(false);
//                    }
//                }
//                //继续加载旧款
//                else if (displayMode == 2) {
//                    if (!ListUtils.isEmpty(mRecommendModel.PassItems)) {
//                        mNewItemAdapter.addPassItem(mRecommendModel.PassItems);
//                        displayMode = 2;
//                        //loadData(false, false);
//                    } else {
//                        //加载完了
//                        mLoadMorePB.setVisibility(View.GONE);
//                        mLoadMoreTxt.setText("没有更多款式了");
//                        mRefreshLayout.setCanLoadMore(false);
//                    }
//                } else if (displayMode == 0) {
//                    //没有新款也没有旧款
//                    mLoadMorePB.setVisibility(View.GONE);
//                    mLoadMoreTxt.setText("没有更多款式了");
//                    mRefreshLayout.setCanLoadMore(false);
//                }
//            }
//            mNewItemBookends.notifyDataSetChanged();
//            if (mLoadingDialog.isShowing()) {
//                mLoadingDialog.stop();
//                mRefreshLayout.scrollTo(0, 0);
//            }
//            if (mRecommendModel.getNextAcvivity() != null &&
//                    mRecommendModel.getNextAcvivity().getID() > 0) {
//                mNextActivitysView.setVisibility(View.VISIBLE);
//                long startMillis = TimeUtils.timeStampToMillis(mRecommendModel.getNextAcvivity().getStartTime());
//                Date startDate = TimeUtils.timeStampToDate(mRecommendModel.getNextAcvivity().getStartTime(), "yyyy-MM-dd HH:mm:ss");
//                Calendar startCal = Calendar.getInstance();
//                startCal.setTime(startDate);
//                int startYear = startCal.get(Calendar.YEAR);
//                int startMonth = startCal.get(Calendar.MONTH);
//                int startDay = startCal.get(Calendar.DATE);
//                int startHour = startCal.get(Calendar.HOUR);
//
//                Calendar cal = Calendar.getInstance();
//                int nowYear = cal.get(Calendar.YEAR);
//                int nowMonth = cal.get(Calendar.MONTH);
//                int nowDay = cal.get(Calendar.DATE);
//                int nowHour = cal.get(Calendar.HOUR);
//
//                if (startYear == nowYear && startMonth == nowMonth) {
//                    if (startDay > nowDay) {
//                        if (startDay - nowDay < 1) {//今天
//                            mTvNextActivity.setText("下一场今天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "后开拼");
//                            return;
//                        } else if (startDay - nowDay < 2) {//明天
//                            mTvNextActivity.setText("下一场明天" + TimeUtils.millisToTimestamp(startMillis, "HH点") + "开拼");
//                            return;
//                        }
//                    }
//                }
//                mTvNextActivity.setText("下一场" + TimeUtils.millisToTimestamp(startMillis, "MM月dd日HH点") + "开拼");
//            } else {
//                mNextActivitysView.setVisibility(View.GONE);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void showEmptyView(boolean show) {
////        mRefreshLayout.setVisibility(show ? View.GONE : View.VISIBLE);
//        mEmptyView.setVisibility(show ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_draw_layout_ok:
//                mDrawerLayout.closeDrawer(drawer_content);
//                setSortData();
//                break;
//            case R.id.tv_draw_layout_clear:
//                if (sortFilterAdpater != null)
//                    sortFilterAdpater.setClear();
//                break;
//            case R.id.btn_next_activity:// 查看拼货预告
//                String url = mRecommendModel.getNextAcvivity().getUrl();
//                if (url.indexOf("/xiaozu/topic/") > 1) {
//                    String temp = "/xiaozu/topic/";
//                    int topicID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                    mContext.startActivity(intent);
//                } else if (url.indexOf("/xiaozu/act/") > 1) {
//                    String temp = "/xiaozu/act/";
//                    int actID = Integer.parseInt(url.substring(url.indexOf(temp) + temp.length()));
//
//                    Intent intent = new Intent(mContext, PostDetailActivity.class);
//                    intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                    intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                    mContext.startActivity(intent);
//                } else {
//                    Intent intent = new Intent(mContext, ItemPreview1Activity.class);
//                    intent.putExtra("name", "拼货预告");
//                    intent.putExtra("url", url);
//                    mContext.startActivity(intent);
//                }
//                break;
//            case R.id.iv_scroll_to_top:
//                mRecyclerView.scrollToPosition(0);
//                break;
//            case R.id.tv_deal:
//                setSortStatus(v.getId());
//                mRecyclerView.scrollToPosition(0);// 防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
//                break;
//            case R.id.tv_shopcat:
//                onFileterCategoryClicked((View) v.getParent());
//                break;
//            case R.id.tv_collect:
//                setSortStatus(v.getId());
//                mRecyclerView.scrollToPosition(0);// 防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
//                break;
//            case R.id.tv_mustdeal:
//                setSortStatus(v.getId());
//                mRecyclerView.scrollToPosition(0);//  防止刷新后，加载提示显示为无更多数据，其实是有数据，可以加载更多的
//                break;
//        }
//    }
//
//    //筛选
//    private void setSortData() {
//        try {
//            List<Boolean> price_is_selects = new ArrayList<>();
//            if (searchPanelBean != null) {
//                if (!ListUtils.isEmpty(searchPanelBean.getPanels())) {
//                    JSONObject jObject = new JSONObject();
//                    JSONArray jsonArray = new JSONArray();
//                    for (SearchPanelBean.PanelsBeanX panelsBeanX : searchPanelBean.getPanels()) {
//                        if (panelsBeanX.getTypeID() == IDsConstant.TYPEID_PRICE) {
//                            if (!ListUtils.isEmpty(panelsBeanX.getPanels())) {
//                                for (SearchPanelBean.PanelsBeanX.PanelsBean panelsBean : panelsBeanX.getPanels()) {
//                                    if (panelsBean.isSelect) {
//                                        price_is_selects.add(true);
//                                    }
//                                }
//                            }
//                        }
//                        JSONObject jsonObject = new JSONObject();
//                        String values = "";
//                        if (!ListUtils.isEmpty(panelsBeanX.getPanels())) {
//                            for (int i = 0; i < panelsBeanX.getPanels().size(); i++) {
//                                if (panelsBeanX.getPanels().get(i).isSelect) {
//                                    if (i == panelsBeanX.getPanels().size() - 1) {
//                                        values = values + panelsBeanX.getPanels().get(i).getID();
//                                    } else {
//                                        values = values + panelsBeanX.getPanels().get(i).getID() + ",";
//                                    }
//                                }
//                            }
//                        }
//                        if (!TextUtils.isEmpty(values)) {
//                            jsonObject.put("TypeID", panelsBeanX.getTypeID());
//                            jsonObject.put("Values", values);
//                            jsonArray.put(jsonObject);
//                        }
//                    }
//                    if (jsonArray.length() > 0) {
//                        jObject.put("Params", jsonArray);
//                    }
//                    //if (price_is_selects.size() <= 0) {
//
//                    if (searchPanelBean.getMinPrice() > 0)
//                        jObject.put("MinPrice", searchPanelBean.getMinPrice());
//                    if (searchPanelBean.getMaxPrice() > 0)
//                        jObject.put("MaxPrice", searchPanelBean.getMaxPrice());
//                    //   }
//
//                    if (jObject.length() <= 0) {
//                        filterValues = "";
//                    } else {
//                        filterValues = jObject.toString();
//                    }
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    loadData(true, true);
//                    mTvs[mCrrentPos].setCompoundDrawables(null, null, nav_choose_sel, null);
//                    mTvs[mCrrentPos].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                    mTvs2[mCrrentPos].setCompoundDrawables(null, null, nav_choose_sel, null);
//                    mTvs2[mCrrentPos].setTextColor(getResources().getColor(R.color.bottom_item_txt_press));
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * @description 过滤分类
//     * @created 2015-3-19 上午10:16:10
//     * @author ZZB
//     */
//    private void onFileterCategoryClicked(View anchorView) {
//        if (mItemShopCategories == null) {
//            ViewHub.showShortToast(mAppContext, "分类信息加载中，请稍等");
//            return;
//        }
//        if (mItemShopCategories.size() == 0) {
//            ViewHub.showShortToast(mAppContext, "暂无分类");
//            return;
//        }
//        String[] items = new String[mItemShopCategories.size()];
//        for (int i = 0; i < items.length; i++) {
//            items[i] = mItemShopCategories.get(i).getName();
//            if (mItemShopCategories.get(i).getId() == mShopCatId) {
//                mCurrentPs = i;
//            }
//        }
//        BottomMenu menu = new BottomMenu(getActivity());
//        menu.setSelectedItemPosition(mCurrentPs);
//        menu.setItems(items).setOnMenuItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                mCurrentPs = position;
//                filterCategory(mItemShopCategories.get(position).getId());
//            }
//        });
//        menu.show(anchorView);
//    }
//
//    /**
//     * @description 过滤分类
//     * @created 2015-3-24 下午6:33:32
//     * @author ZZB
//     */
//    private void filterCategory(int catId) {
//        mShopCatId = catId;
//        onRefresh();
//    }
//
//    //tag = 1 倒序         tag = 0 正序
//    private void setSortStatus(int resId) {
//        switch (resId) {
//            case R.id.tv_deal:
//                if (mSortIndex != Const.SortIndex.DealCountDesc) {
//                    mSortIndex = Const.SortIndex.DealCountDesc;
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    resetSortView();
//                    loadData(true, true);
//                }
//                break;
//            case R.id.tv_collect:
//                if (mSortIndex != Const.SortIndex.CollectCountDesc) {
//                    mSortIndex = Const.SortIndex.CollectCountDesc;
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    resetSortView();
//                    loadData(true, true);
//                }
//                break;
//            case R.id.tv_mustdeal:
//                if (mSortIndex != Const.SortIndex.MustDealDesc) {
//                    mSortIndex = Const.SortIndex.MustDealDesc;
//                    mNewItemAdapter.clear();
//                    displayMode = 0;
//                    resetSortView();
//                    loadData(true, true);
//                }
//                break;
//        }
//    }
//
//    private void resetSortView() {
//        switch (mSortIndex) {
//            case DealCountDesc:
//                tvDealSort.setTextColor(mColorBlue);
//                tvCollectSort.setTextColor(mColorGray);
//                tvMustDealSort.setTextColor(mColorGray);
//                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
//                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                break;
//            case CreateTimeDesc:
//                tvDealSort.setTextColor(mColorGray);
//                tvCollectSort.setTextColor(mColorGray);
//                tvMustDealSort.setTextColor(mColorGray);
//                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                break;
//            case CollectCountDesc:
//                tvDealSort.setTextColor(mColorGray);
//                tvCollectSort.setTextColor(mColorBlue);
//                tvMustDealSort.setTextColor(mColorGray);
//                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
//                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                break;
//            case MustDealDesc:
//                tvDealSort.setTextColor(mColorGray);
//                tvCollectSort.setTextColor(mColorGray);
//                tvMustDealSort.setTextColor(mColorBlue);
//                tvDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvCollectSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down_gray, 0);
//                tvMustDealSort.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
//                break;
//        }
//    }
//
//    private class MyCountDownTimer extends CountDownTimer {
//        public MyCountDownTimer(long millisInFuture) {
//            super(millisInFuture, 100);
//        }
//
//        @Override
//        public void onTick(long millisUntilFinished) {
//            long millisUntilFinished_now = mRecommendModel.getInfo().getEndMillis() - System.currentTimeMillis();
//            int hour = (int) (millisUntilFinished_now / TimeUtils.HOUR_MILLIS);
//            int min = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS) / TimeUtils.MINUTE_MILLIS);
//            int sec = (int) ((millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS) / TimeUtils.SECOND_MILLIS);
//            int milli = (int) (millisUntilFinished_now - hour * TimeUtils.HOUR_MILLIS - min * TimeUtils.MINUTE_MILLIS - sec * TimeUtils.SECOND_MILLIS);
//            updateCountDownTime(hour, min, sec, milli);
//        }
//
//        @Override
//        public void onFinish() {
//            try {
//                mTvOveredTips.setVisibility(View.VISIBLE);
//                mTvOveredTips.setText("本场已结束");
//                headView.findViewById(R.id.lladd).findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                mFloatHeadView.findViewById(R.id.tv_overed_tips).setVisibility(View.VISIBLE);
//                mFloatHeadView.findViewById(R.id.ll_top1).setVisibility(View.GONE);
//                ((TextView) mFloatHeadView.findViewById(R.id.tv_overed_tips)).setText("本场已结束");
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void updateCountDownTime(int hour, int min, int sec, int milli) {
//        setHourTv(mTvHH, mTvH, mTvDay, mTvDayDesc, hour);
////        setTv(mTvHH, mTvH, hour);
//        setTv(mTvMM, mTvM, min);
//        setTv(mTvSS, mTvS, sec);
//        mTvF.setText(milli / 100 + "");
//
//        setHourTv(mTvFHH, mTvFH, mTvFDay, mTvFDayDesc, hour);
////        setTv(mTvFHH, mTvFH, hour);
//        setTv(mTvFMM, mTvFM, min);
//        setTv(mTvFSS, mTvFS, sec);
//        mTvFF.setText(milli / 100 + "");
//    }
//
//    private void setHourTv(TextView tvHH, TextView tvH, TextView tvDay, TextView tvDayDesc, int time) {
//        int main_bottom_selector = time / 10;
//        if (time < 24) {
//            int a = time % 10;
//            tvHH.setText(main_bottom_selector + "");
//            tvH.setText(a + "");
//            tvDay.setVisibility(View.GONE);
//            tvDayDesc.setVisibility(View.GONE);
//        } else {
//            tvDay.setVisibility(View.VISIBLE);
//            tvDayDesc.setVisibility(View.VISIBLE);
//            int hours = time % 24;
//            setTv(tvHH, tvH, hours);
//            tvDay.setText((time / 24) + "");
//        }
//
//    }
//
//    private void setTv(TextView tvAA, TextView tvA, int time) {
//        int main_bottom_selector = time / 10;
//        int a = time % 10;
//        tvAA.setText(main_bottom_selector + "");
//        tvA.setText(a + "");
//    }
//
//    public void search(String keyword) {
//        mPinHuoModel.keyword = keyword;
//        mNeedToClearKeyword = false;
//        mRefreshLayout.manualRefresh();
//    }
//
//
//    // 根据userid获取首页数据
//    private class GetShopCategoriesTask extends AsyncTask<Void, Void, Object> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            mLoadingDialog.start(getString(R.string.items_loadData_loading));
//        }
//
//        @Override
//        protected Object doInBackground(Void... params) {
//            try {
//                mItemShopCategories = ShopSetAPI
//                        .getItemCatsByShopID(mAppContext, mRecommendModel.getInfo().getShopID());
//                return "OK";
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "error:" + e.getMessage();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Object result) {
//            super.onPostExecute(result);
//            if (result instanceof String && ((String) result).startsWith("error:")) {
//                ViewHub.showLongToast(mAppContext, ((String) result).replace("error:", ""));
//            }
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            mSortIndex = Const.SortIndex.DefaultDesc;
//            if (mLoadingDialog != null) {
//                mLoadingDialog.dismiss();
//                mLoadingDialog = null;
//            }
//        } catch (Exception e) {
//            mLoadingDialog = null;
//        }
//    }
//
//    // 返回键按下时会被调用
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == event.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            // TODO
//            if (mDrawerLayout != null) {
//                if (mDrawerLayout.isDrawerOpen(drawer_content)) {
//                    mDrawerLayout.closeDrawer(drawer_content);
//                } else {
//                    getActivity().finish();
//                }
//            } else {
//                getActivity().finish();
//            }
//        }
//        return false;
//    }
//}
