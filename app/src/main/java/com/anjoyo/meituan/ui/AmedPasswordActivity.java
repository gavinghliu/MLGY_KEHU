package com.anjoyo.meituan.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.domain.User;
import com.anjoyo.meituan.utils.SocketUtils;
import com.anjoyo.meituan.utils.SocketUtils.SocketListener;
import com.anjoyo.mlgy.R;

public class AmedPasswordActivity extends BaseActivity implements
		OnClickListener {
	private ImageView imageview_back;
	private TextView textview_back;
	private EditText et_oldpassword, et_newpassword, et_newpassword2;
	private Button confirm;
	private String username;
	private User user;
	private AppContext appContext;
	private SocketUtils socketUtil;
	public ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	void init() {
		setContentView(R.layout.amedpassword_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_aboutmeituanback);
		textview_back = (TextView) findViewById(R.id.textview_meituan);
		et_oldpassword = (EditText) findViewById(R.id.oldpassword);
		et_newpassword = (EditText) findViewById(R.id.newpassword);
		et_newpassword2 = (EditText) findViewById(R.id.newpassword2);
		confirm = (Button) findViewById(R.id.button_confirm);
		
		socketUtil = new SocketUtils(this);
		confirm.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		textview_back.setOnClickListener(this);
		dialog = new ProgressDialog(this);

		appContext = (AppContext) getApplicationContext();
		user = appContext.getUser();
		username = user.getUsername();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_aboutmeituanback:
		case R.id.textview_meituan:
			finish();
			break;
		case R.id.button_confirm:
			
			String oldpassword = et_oldpassword.getText().toString();
			String newpassword = et_newpassword.getText().toString();
			String newpassword2 = et_newpassword2.getText().toString();

			if (oldpassword.equals("") || oldpassword.equals("")) {
				Toast.makeText(AmedPasswordActivity.this, "密码不可为空",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (newpassword.equals(newpassword2)) {
				
				if (isNumeric(newpassword)) {
					Toast.makeText(AmedPasswordActivity.this, "密码为英文+数字，不能全为数字",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				String qianzhui = "yggm_";
				if (user.getType() == 2) {
					qianzhui = "khgm_";
				} 
				
				String requestString = qianzhui + username + "_" + appContext.getSIME()
						+ "_" + oldpassword + "_" + newpassword;

				// 发送请求 khgm_+手机号+手机码+旧密码+新密码
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
								
								if (respone.contains("更改密码成功")) {
									Intent intent = new Intent(AmedPasswordActivity.this,
											LoginActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
									appContext.setUser(null);
								}
								Toast.makeText(AmedPasswordActivity.this, respone,
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
								Toast.makeText(AmedPasswordActivity.this, "更改密码失败",
										Toast.LENGTH_SHORT).show();
							}
						});
						
					}

					@Override
					public void timeOut() {
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						Toast.makeText(AmedPasswordActivity.this, "网络请求超时",
								Toast.LENGTH_SHORT).show();
						
					}
				});
				onCreateDialog();
			} else {
				Toast.makeText(this, "两次输入密码不正确，请重新输入", Toast.LENGTH_SHORT)
						.show();
				et_newpassword.setText("");
				et_newpassword2.setText("");
			}

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
	
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
}
