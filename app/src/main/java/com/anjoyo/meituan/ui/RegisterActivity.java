package com.anjoyo.meituan.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.mlgy.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	
	static public String REGISTER_STATE = "register_state";
	static public int STATE_REGISTER = 1;
	static public int STATE_HUANJI = 2;
	static public int STATE_WANJIMIMA = 3;
	static public int STATE_XIUGAIMIMA = 4;
	
	private EditText edittext_username, edittext_password, edittext_password2,
			edittext_check, editText_jiumima;
	private Button button_confirm, btn_check;
	private ImageView imageview_back;
	private TextView textview_back;
	private SocketUtils socketUtil;
	public ProgressDialog dialog;
	private RadioButton mRdYuangong;
	private RadioButton mRdKehu;
	private TextView mTvJiumima, mTvXinmima;
	private TextView mChongfuTv, mHuanjiTv;
	
	
	private int state;  // 1注册 2换机 3忘记密码  //4修改密码
	
	private TimeCount time;
	
	private String mName;
	private String mPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		socketUtil = new SocketUtils(this);
		dialog = new ProgressDialog(this);
	}

	@Override
	void init() {
		setContentView(R.layout.register_activity);
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
		button_confirm = (Button) findViewById(R.id.button_confirm);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		textview_back = (TextView) findViewById(R.id.textview_meituan);
		edittext_password2 = (EditText) findViewById(R.id.edittext_password2);
		edittext_check = (EditText) findViewById(R.id.edittext_check);
		btn_check = (Button) findViewById(R.id.btn_check);
		mTvJiumima = (TextView) findViewById(R.id.tv_jiumima);
		mTvXinmima = (TextView) findViewById(R.id.tv_xinmima);
		editText_jiumima = (EditText) findViewById(R.id.edittext_password_jiu);
		mChongfuTv = (TextView) findViewById(R.id.tv_chongfu);
		mHuanjiTv = (TextView) findViewById(R.id.tv_huanji);
		
		mTvJiumima.setVisibility(View.GONE);
		editText_jiumima.setVisibility(View.GONE);
		mHuanjiTv.setVisibility(View.GONE);
		
		mRdYuangong = (RadioButton) findViewById(R.id.yuangong);
		mRdKehu = (RadioButton) findViewById(R.id.kehu);
		button_confirm.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		textview_back.setOnClickListener(this);
		btn_check.setOnClickListener(this);
		
		state = STATE_REGISTER;
		
		if (null != getIntent() && null != getIntent().getExtras()) {
			state = getIntent().getExtras().getInt(REGISTER_STATE, STATE_REGISTER);
		}
		
		if (state == STATE_HUANJI) {
			mChongfuTv.setVisibility(View.GONE);
			edittext_password2.setVisibility(View.GONE);
			mHuanjiTv.setVisibility(View.VISIBLE);
		} else if (state == STATE_WANJIMIMA) {
			mTvJiumima.setVisibility(View.VISIBLE);
			editText_jiumima.setVisibility(View.VISIBLE);
			mTvJiumima.setVisibility(View.GONE);
			editText_jiumima.setVisibility(View.GONE);
		}
		
		if (AppContext.APP_TYPE == 1) {
			mRdYuangong.setVisibility(View.GONE);
			mRdKehu.setChecked(true);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.textview_meituan:
			finish();
			break;
		case R.id.btn_check:
			String qianzhui = "ygyz_";
			if (!mRdYuangong.isChecked()) {
				qianzhui = "khyz_";
			} 
			
			if (state == STATE_HUANJI) {
				qianzhui = "yghj_";
				if (!mRdYuangong.isChecked()) {
					qianzhui = "khhj_";
				} 
			} else if (state == STATE_WANJIMIMA) {
				qianzhui = "ygwj_";
				if (!mRdYuangong.isChecked()) {
					qianzhui = "khwj_";
				} 
			}
			
			// khyz+手机号
			String username = edittext_username.getText().toString();
			if (username.length() == 0) {
				Toast.makeText(RegisterActivity.this, "用户名为空，请输入",
						Toast.LENGTH_SHORT).show();
				return;
			}
			final AppContext appContext = (AppContext) getApplicationContext();
			String requestString = qianzhui + username + "_" + appContext.getSIME();
			// 发送请求
			socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString, SocketUtils.ZHUCHE_REQUEST2 + qianzhui+ username, new SocketListener() {

				@Override
				public void downLoadSuccess(final String respone) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							
							if (respone.contains("短信发送中")) {
								btn_check.setText("已获取验证码");
								btn_check.setClickable(false);
								time = new TimeCount(60000, 1000);//构造CountDownTimer对象
								time.start();//开始计时
							} 
							Toast.makeText(RegisterActivity.this, respone,
									Toast.LENGTH_SHORT).show();
							
						}
					});
				}
				@Override
				public void timeOut() {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
					Toast.makeText(RegisterActivity.this, "网络请求超时",
							Toast.LENGTH_SHORT).show();
					
				}
				@Override
				public void downLoadFail() {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
							Toast.makeText(RegisterActivity.this, "注册失败",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			onCreateDialog();
			break;
		case R.id.button_confirm:// 确认注册
			
			qianzhui = "ygzc_";
			if (!mRdYuangong.isChecked()) {
				qianzhui = "khzc_";
			} 
			
			if (state == STATE_HUANJI) {
				qianzhui = "yggj_";
				if (!mRdYuangong.isChecked()) {
					qianzhui = "khgj_";
				} 
			} else if (state == STATE_WANJIMIMA) {
				qianzhui = "ygrm_";
				if (!mRdYuangong.isChecked()) {
					qianzhui = "khrm_";
				} 
			}
			
			username = edittext_username.getText().toString();
			mName = username;
			final String password = edittext_password.getText().toString();
			final String confirmPwd = edittext_password2.getText().toString();
			final String numberCheck = edittext_check.getText().toString();

			if (state != STATE_HUANJI && !confirmPwd.equals(password)) {
				Toast.makeText(RegisterActivity.this, "两次输入密码不一样",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (isNumeric(password)) {
				Toast.makeText(RegisterActivity.this, "密码为英文+数字，不能全为数字",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			mPassword = password;
			final AppContext appContext2 = (AppContext) getApplicationContext();
			
			requestString = qianzhui + username + "_" + appContext2.getSIME()
					+ "_" + password + "_" + numberCheck;

			// khzc+手机号+手机码+密码+验证码
			if (!username.equals("") && !password.equals("")) {
				// 发送请求
				socketUtil.sendRequest(SocketUtils.ZHUCHE_REQUEST + requestString, SocketUtils.ZHUCHE_REQUEST2 + qianzhui + username
				, new SocketListener() {

					@Override
					public void downLoadSuccess(final String respone) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								
								if (STATE_REGISTER == state) {
									if (respone.contains("注册成功")) {
										User user = new User();
										user.setUsername(mName);
										user.setPassword(mPassword);
										int type = 1;
										if (!mRdYuangong.isChecked()) {
											type = 2;
										}
										user.setType(type);
										((AppContext) getApplicationContext()).setUser(user);
										final Intent intent = new Intent(RegisterActivity.this,
												MainActivity.class);
										startActivity(intent);
										
										appContext2.getPreferences().edit().putLong(WelcomeActivity.LONGIN_TIME, System.currentTimeMillis()).commit();
										finish();
									}
								} else if (STATE_HUANJI== state) {
									if (respone.contains("更换手机成功")) {
										final Intent intent = new Intent(RegisterActivity.this,
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								} else if (STATE_WANJIMIMA == state) {
									if (respone.contains("密码重置成功")) {
										final Intent intent = new Intent(RegisterActivity.this,
												LoginActivity.class);
										startActivity(intent);
										finish();
									}
								}
								
								Toast.makeText(RegisterActivity.this, respone,
										Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(RegisterActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
					
					@Override
					public void downLoadFail() {
						
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								Toast.makeText(RegisterActivity.this, "注册失败",
										Toast.LENGTH_SHORT).show();
							}
						});
						
					}
				});
			} else {
				Toast.makeText(RegisterActivity.this, "账号或者密码为空",
						Toast.LENGTH_SHORT).show();
			}
			onCreateDialog();
			break;
		default:
			break;
		}
	}

	public void onCreateDialog() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View v = inflater
				.inflate(R.layout.selfdef_progress_dialog_layout, null);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setIndeterminate(true);
		dialog.show();
		dialog.setContentView(v);
	}
	
	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btn_check.setText("重新验证");
			btn_check.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btn_check.setClickable(false);
			btn_check.setText(millisUntilFinished / 1000 + "秒后重新获取验证码");
		}
	}

	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
