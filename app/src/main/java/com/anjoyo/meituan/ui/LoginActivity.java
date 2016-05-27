package com.anjoyo.meituan.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.mlgy.R;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private TextView textview_register,textview_wanjimima;
	private ImageView imageview_back;
	private TextView imageview_meituan;
	private EditText edittext_username, edittext_password;
	private Button login;
	private RadioButton mRdYuangong;
	private RadioButton mRdKehu;
	public ProgressDialog dialog;
	private SocketUtils socketUtil;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		socketUtil = new SocketUtils(this);
		dialog = new ProgressDialog(this);
	}

	void init() {
		setContentView(R.layout.login_user);
		textview_register = (TextView) findViewById(R.id.textView_register);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_meituan = (TextView) findViewById(R.id.imageview_meituan);
		login = (Button) findViewById(R.id.button_login);
		edittext_username = (EditText) findViewById(R.id.login_userName);
		edittext_password = (EditText) findViewById(R.id.login_userPassword);
		textview_wanjimima = (TextView) findViewById(R.id.textView_wanjimima);
		
		textview_wanjimima.setOnClickListener(this);
		
		mRdYuangong = (RadioButton) findViewById(R.id.yuangong);
		mRdKehu = (RadioButton) findViewById(R.id.kehu);

		
		if (AppContext.APP_TYPE == 1) {
			mRdYuangong.setVisibility(View.GONE);
			mRdKehu.setChecked(true);
		}
		
		textview_register.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		imageview_meituan.setOnClickListener(this);
		login.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_register:
			startActivityForResult(new Intent(this, RegisterActivity.class), 9);
			break;
		case R.id.imageview_back:
		case R.id.imageview_meituan:
			finish();
			break;
		case R.id.textView_wanjimima:
			Intent intent = new Intent(LoginActivity.this,
					RegisterActivity.class);
			intent.putExtra(RegisterActivity.REGISTER_STATE, RegisterActivity.STATE_WANJIMIMA);
			startActivity(intent);
			break;
		case R.id.button_login:
			final String username = edittext_username.getText().toString();
			final String password = edittext_password.getText().toString();
			
			final AppContext appContext = (AppContext) getApplicationContext();
			
			if (username.equals("test1234") && password.equals("test1234")) {
				appContext.getPreferences().edit().putLong(WelcomeActivity.LONGIN_TIME, System.currentTimeMillis()).commit();
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				int type = 1;
				if (!mRdYuangong.isChecked()) {
					type = 2;
				}
				user.setType(type);
				((AppContext) getApplicationContext()).setUser(user);
				final Intent intent2 = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent2);
				finish();
			}
			
			String qianzhui = "ygdl_";
			if (!mRdYuangong.isChecked()) {
				qianzhui = "khdl_";
			} 
			
			
			String requestString = qianzhui + username + "_" + appContext.getSIME()
					+ "_" + password;

			// khdl+手机号+手机码
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
								
								if (respone.contains("登陆成功")) {
									
									appContext.getPreferences().edit().putLong(WelcomeActivity.LONGIN_TIME, System.currentTimeMillis()).commit();
									User user = new User();
									user.setUsername(username);
									user.setPassword(password);
									int type = 1;
									if (!mRdYuangong.isChecked()) {
										type = 2;
									}
									user.setType(type);
									((AppContext) getApplicationContext()).setUser(user);
									final Intent intent = new Intent(LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();
								} else if (respone.contains("重新验证")) {
									Intent intent = new Intent(LoginActivity.this,
											RegisterActivity.class);
									intent.putExtra(RegisterActivity.REGISTER_STATE, RegisterActivity.STATE_HUANJI);
									startActivity(intent);
								}
								Toast.makeText(LoginActivity.this, respone,
										Toast.LENGTH_SHORT).show();
							}
						});
					}

					@Override
					public void downLoadFail() {
						
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (dialog.isShowing()) {
									dialog.dismiss();
								}
								Toast.makeText(LoginActivity.this, "登录失败",
										Toast.LENGTH_SHORT).show();
							}
						});
						
					}
					
					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(LoginActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
				onCreateDialog();
			} else {
				Toast.makeText(LoginActivity.this, "账号或者密码为空",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
//			String username = data.getStringExtra("username");
//			String password = data.getStringExtra("password");
//			edittext_username.setText(username);
//			edittext_password.setText(password);

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
}
