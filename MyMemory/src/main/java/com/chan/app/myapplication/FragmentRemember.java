package com.chan.app.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.app.utils.Memory;
import com.chan.app.utils.MyBmobUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.bmob.v3.listener.SaveListener;

public class FragmentRemember extends Fragment implements OnClickListener {

    private View v;
    private ImageView iv_insertPic;
    private ImageView iv_insertPicByCamera;
    private ImageView iv_insertMusic;
    private ImageView iv_insertVideo;
    private TextView tv_clearText;
    private TextView tv_editFinish;
    private EditText editText;

    private static final int REQUEST_CODE_PICK_IMAGE = 0; //启动相册选择图片的请求码
    private static final int REQUEST_CODE_CAPTURE_CAMERA = 1; //启动相机拍照的请求码

    private static Bitmap chooseBitmap; // 选择好的图片
    private static String chooseBitmapImagepath; //选择好的图片的路径

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_remember, container, false);
        initAllButton();
        return v;
    }

    /*
     * 初始化所有按钮
     */
    private void initAllButton() {
        editText = (EditText) v.findViewById(R.id.edittext);
        tv_clearText = (TextView) v.findViewById(R.id.tv_clearText);
        tv_editFinish = (TextView) v.findViewById(R.id.tv_editFinish);
        iv_insertPic = (ImageView) v.findViewById(R.id.iv_insertPic);
        iv_insertPicByCamera = (ImageView) v.findViewById(R.id.iv_insertPicByCamera);
        iv_insertMusic = (ImageView) v.findViewById(R.id.iv_insertMusic);
        iv_insertVideo = (ImageView) v.findViewById(R.id.iv_insertVideo);

        tv_clearText.setOnClickListener(this);
        tv_editFinish.setOnClickListener(this);
        iv_insertPic.setOnClickListener(this);
        iv_insertPicByCamera.setOnClickListener(this);
        iv_insertMusic.setOnClickListener(this);
        iv_insertVideo.setOnClickListener(this);
    }

    /*
     * 按钮监听点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_insertPic:
                getImageFromAlbum();
                break;
            case R.id.iv_insertPicByCamera:
                getImageFromCamera();
                break;
            case R.id.iv_insertMusic:

                break;
            case R.id.iv_insertVideo:

                break;
            case R.id.tv_clearText:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("确认清楚全部已编辑的内容吗?");
                builder.setPositiveButton("清除", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText("");
                    }
                });
                builder.setNegativeButton("返回", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                break;

            case R.id.tv_editFinish:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                builder2.setCancelable(true);
                builder2.setMessage("完成记录吗?");
                builder2.setPositiveButton("完成", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String allMsg = editText.getText().toString();
                        final Memory memory  = new Memory();
                        memory.setAllMsg(allMsg);
                        memory.save(getActivity(), new SaveListener() {
                            @Override
                            public void onSuccess() {
                                editText.setText("");
                                Toast.makeText(getActivity(), "保存成功,该条记录时间为:" + memory.getCreatedAt(), Toast
                                        .LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder2.setNegativeButton("返回", new AlertDialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder2.show();
                break;

            default:
                break;
        }
    }

    /*
     * 启动相册获取图片
     */
    private void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // 设置图片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /*
     * 启动相机应用获取图片
     */
    private void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMERA);
        } else {
            Toast.makeText(getActivity(), "sd卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 处理返回结果
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                // androi4.4版本以上处理图片
                if (Build.VERSION.SDK_INT >= 19) {
                    getActivity();
                    if (resultCode == Activity.RESULT_OK) {
                        // 选择成功
                        chooseBitmap = handleImageOnKitKat(data);
                        if (chooseBitmap != null) {
                            // 将选中的图片插入edittext
                            insertBitmapInEditText(chooseBitmap, editText);
                        }
                    } else {
                        getActivity();
                        if (resultCode == Activity.RESULT_CANCELED) {
                            // 取消选择
                            chooseBitmap = null;
                        }
                    }
                } else {
                    getActivity();
                    if (resultCode == Activity.RESULT_OK) {
                        // 4.4版本以下处理图片
                        chooseBitmap = handleImageBeforeKitKat(data);
                    }
                }
                break;
            case REQUEST_CODE_CAPTURE_CAMERA:
                Uri uri = data.getData();
                if (resultCode == getActivity().RESULT_OK) {
                    if (uri == null) {
                        // uri为空，则从bundle中获取
                        Bundle bundle = data.getExtras();
                        System.out.println("bundle----" + bundle);
                        if (bundle != null) {
                            Bitmap photo = (Bitmap) bundle.get("data");
                            chooseBitmap = saveImage(photo);
                            insertBitmapInEditText(chooseBitmap, editText);
                        } else {
                            Toast.makeText(getActivity(), "��ȡ��Ƭʧ��", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        // uri不为空则按一般方法获取
                        String imagePath = getImagePath(uri, null);
                        chooseBitmap = BitmapFactory.decodeFile(imagePath);
                        insertBitmapInEditText(chooseBitmap, editText);
                    }
                } else {
                    getActivity();
                    if (resultCode == Activity.RESULT_CANCELED) {
                        // 取消拍照
                        chooseBitmap = null;
                    }
                }

                break;

            default:
                break;
        }
    }

    /*
     * 将图片插入到EditText中
     */
    private void insertBitmapInEditText(Bitmap chooseBitmap, EditText editText) {
        ImageSpan imageSpan = new ImageSpan(getActivity(), chooseBitmap, ImageSpan.ALIGN_BOTTOM);
        SpannableString spannableString = new SpannableString(chooseBitmapImagepath);
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        editText.append(spannableString);
    }

    /*
     * 4.4�����ϰ汾����ͼƬ
     */
    private Bitmap handleImageOnKitKat(Intent data) {

        String imagePath = null;
        Uri uri = data.getData();
        // 如果是Document类型的Uri
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                String id = docId.split(":")[1];
                String selection = Media._ID + "=" + id;
                imagePath = getImagePath(Media.EXTERNAL_CONTENT_URI, selection);
            } else if (uri.getAuthority().equals("com.android.providers.downloads.documents")) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long
                        .valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if (uri.getScheme().equalsIgnoreCase("content")) {
            imagePath = getImagePath(uri, null);
        }

        Bitmap bitmap = null;

        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } else {
            Toast.makeText(getActivity(), "图片获取失败", Toast.LENGTH_SHORT).show();
        }

        chooseBitmapImagepath = imagePath;

        return smallBitmap(bitmap);//返回缩小后的图片

    }

    /*
     * 4.4以下版本处理图片
     */
    private Bitmap handleImageBeforeKitKat(Intent data) {
        Bitmap bitmap = null;
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        bitmap = BitmapFactory.decodeFile(imagePath);
        Bitmap smallerBitmap = smallBitmap(bitmap);
        chooseBitmapImagepath = imagePath;
        return smallerBitmap;
    }

    /*
     * 获取图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /*
     * 缩小图片
     */
    private Bitmap smallBitmap(Bitmap bitmap) {

        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        Bitmap smallBitmap = null;

        // 判断图片是否过大
        if (bitmap.getWidth() > 800 || bitmap.getHeight() > 800) {
            smallBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else {
            return bitmap;
        }

        return smallBitmap;
    }

    /*
       放大图片
     */
    private Bitmap largeBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(2.0f, 2.0f);

        Bitmap largerBitmap;
        //若图片过小
        if (bitmap.getWidth() < 500 || bitmap.getHeight() < 500) {
            largerBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } else {
            return bitmap;
        }

        return largerBitmap;
    }

    /*
     * 保存图片
     */
    private Bitmap saveImage(Bitmap photo) {
        Bitmap chooseBitmap = null;
        File tmpDirFile = new File(Environment.getExternalStorageDirectory().toString() + "/memory");
        if (!tmpDirFile.exists()) {
            tmpDirFile.mkdir();
        }
        File imgFile = new File(tmpDirFile, System.currentTimeMillis() + ".png");
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imgFile, false));
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            chooseBitmap = photo;
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return largeBitmap(chooseBitmap);
    }

}
