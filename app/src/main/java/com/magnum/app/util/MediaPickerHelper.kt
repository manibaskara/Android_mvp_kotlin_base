package com.magnum.app.util

import android.app.ActionBar
import android.app.Dialog
import android.net.Uri
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.FragmentActivity
import com.magnum.app.R
import com.magnum.app.common.Constants.MaxValues.Companion.MAX_VIDEO_DURATION
import com.magnum.app.common.Constants.MediaMaxCount.Companion.GALLERY_COLUMN_COUNT
import com.magnum.app.common.Constants.MediaMaxCount.Companion.MAX_MEDIA_COUNT
import com.developers.imagezipper.ImageZipper
import com.yanzhenjie.album.Album
import com.yanzhenjie.album.AlbumFile
import com.yanzhenjie.album.api.widget.Widget
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MediaPickerHelper(
    private var mActivity: FragmentActivity,
    private var mediaPickerListener: MediaPickerListener
) {

    private var btnCamera: AppCompatTextView? = null
    private var btnGallery: AppCompatTextView? = null
    private var dialog: Dialog? = null
    private var codeSnippet: CodeSnippet = CodeSnippet(mActivity)

    init {
        createDialog()
    }

    private fun createDialog() {
        dialog = Dialog(mActivity)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.inflate_camera_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        )
        btnCamera = dialog?.findViewById(R.id.btnCamera) as AppCompatTextView
        btnGallery = dialog?.findViewById(R.id.btnGallery) as AppCompatTextView

    }
/*
    ---------------------------------------------------------------------------
    Pickers
*/

    fun showSingleImagePicker() {

        btnGallery?.setOnClickListener {
            selectSingleImage()
            dialog?.dismiss()
        }
        btnCamera?.setOnClickListener {
            captureSingleImage()
            dialog?.dismiss()
        }
        dialog?.show()
    }

    fun showSingleVideoPicker() {
        btnGallery?.setOnClickListener {
            chooseSingleVideo()
            dialog?.dismiss()
        }
        btnCamera?.setOnClickListener {
            takeVideoFromCamera()
            dialog?.dismiss()
        }
        dialog?.show()

    }

    fun showMultipleImageVideoPicker() {
        btnGallery?.setOnClickListener {
            chooseMultipleVideosPhotos()
            dialog?.dismiss()
        }
        btnCamera?.setOnClickListener {
            takePhotoOrVideoFromCamera()
            dialog?.dismiss()
        }
        dialog?.show()
    }

    fun showSingleImageVideoPicker() {

        btnGallery?.setOnClickListener {
            chooseSingleVideosPhotos()
            dialog?.dismiss()
        }
        btnCamera?.setOnClickListener {
            takePhotoOrVideoFromCamera()
            dialog?.dismiss()
        }
        dialog?.show()
    }

/*
    ---------------------------------------------------------------------------
    Actions
*/

    private fun takeVideoFromCamera() {
        Album.camera(mActivity)
            .video()
            .quality(1) // Video quality, [0, 1].
            .limitDuration(MAX_VIDEO_DURATION)
            .limitBytes(Long.MAX_VALUE)
            .onResult { result -> mediaPickerListener.onSuccess(getUriFromPath(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun chooseSingleVideo() {

        Album.video(mActivity) // Video selection.
            .singleChoice()
            .camera(false)
            .columnCount(GALLERY_COLUMN_COUNT)
            .widget(getWidgetTheme())
            .afterFilterVisibility(true)
            .onResult { result -> mediaPickerListener.onSuccess(getUriListFromAlbumList(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun takePhotoOrVideoFromCamera() {

        Album.camera(mActivity) // Camera function.
            .image() // Take Picture.
            .onResult { result -> mediaPickerListener.onSuccess(getUriFromPath(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun chooseMultipleVideosPhotos() {

        Album.album(mActivity)
            .multipleChoice()
            .selectCount(MAX_MEDIA_COUNT)
            .camera(false)
            .columnCount(GALLERY_COLUMN_COUNT)
            .widget(getWidgetTheme())
            .onResult { result -> mediaPickerListener.onSuccess(getUriListFromAlbumList(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun chooseSingleVideosPhotos() {

        Album.album(mActivity)
            .singleChoice()
            .camera(false)
            .columnCount(GALLERY_COLUMN_COUNT)
            .widget(getWidgetTheme())
            .onResult { result -> mediaPickerListener.onSuccess(getUriListFromAlbumList(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun selectSingleImage() {

        Album.image(mActivity)
            .singleChoice()
            .camera(false)
            .columnCount(GALLERY_COLUMN_COUNT)
            .widget(getWidgetTheme())
            .afterFilterVisibility(true)
            .onResult { result -> mediaPickerListener.onSuccess(getUriListFromAlbumList(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun getWidgetTheme(): Widget {

        return Widget.newDarkBuilder(mActivity.baseContext)
            .title("Select Image/videos")
            .statusBarColor(codeSnippet.getColor(R.color.colorPrimaryDark))
            .toolBarColor(codeSnippet.getColor(R.color.colorPrimary))
            .navigationBarColor(codeSnippet.getColor(R.color.colorPrimaryDark))
            .mediaItemCheckSelector(
                codeSnippet.getColor(R.color.grey),
                codeSnippet.getColor(R.color.colorPrimaryDark)
            )
            .bucketItemCheckSelector(
                codeSnippet.getColor(R.color.grey),
                codeSnippet.getColor(R.color.colorPrimaryDark)
            )
            .build()
    }

    private fun captureSingleImage() {
        Album.camera(mActivity) // Camera function.
            .image() // Take Picture.
            .onResult { result -> mediaPickerListener.onSuccess(getUriFromPath(result)) }
            .onCancel { result -> mediaPickerListener.onError(result) }
            .start()
    }

    private fun getUriListFromAlbumList(albumList: ArrayList<AlbumFile>): MutableList<Uri> {
        val uriList = mutableListOf<Uri>()
        for (i in 0 until albumList.size) {
            var fromFile = Uri.fromFile(File(albumList[i].path))
            if (fromFile.path?.isNotEmpty() == true) {
                fromFile = compressFile(fromFile)
            }
            uriList.add(fromFile)
        }
        return uriList
    }

    private fun getUriFromPath(path: String): MutableList<Uri> {
        val uriList = mutableListOf<Uri>()
        val returnUri = compressFile(Uri.fromFile(File(path)))
        returnUri?.let {
            uriList.add(it)
        }
        return uriList
    }

    private fun compressFile(fileUri: Uri): Uri? {
        var outputUri: Uri? = null
        if (fileUri.path != null) {
            fileUri.path?.let { path ->
                val inputFileSize = Integer.parseInt((File(path).length() / 1024).toString())
                Timber.d("fileSize inputFileSize = $inputFileSize KB")
                val mimeType = codeSnippet.getMimeType(path)
                if (mimeType?.contains("image") == true) {
                    val fileName =
                        SimpleDateFormat("yyyyMMddHHmm'.jpg'", Locale.ENGLISH).format(Date())
                    val filesDir: File = mActivity.filesDir
                    var imageFile = File(filesDir, fileName)
                    imageFile.createNewFile()
                    imageFile =
                        ImageZipper(mActivity.applicationContext).compressToFile(File(path))
                    outputUri = Uri.fromFile(File(imageFile.path))
                    val outPutFileSize = Integer.parseInt((imageFile.length() / 1024).toString())
                    Timber.d("fileSize outPutFileSize = $outPutFileSize KB")
                } else {
                    outputUri = fileUri
                }
            }
        } else {
            outputUri = fileUri
        }
        return outputUri
    }
}