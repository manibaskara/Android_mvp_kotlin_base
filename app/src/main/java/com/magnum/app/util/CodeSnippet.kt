@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.magnum.app.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import android.text.SpannableString
import android.text.Spanned
import android.text.format.Time
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.magnum.app.R
import com.magnum.app.common.Constants.GlobalTimeFormat.Companion.DATE_TIME_FORMAT
import com.magnum.app.view.widgets.CircularImageView
import com.squareup.moshi.Moshi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern
import kotlin.math.roundToInt

class CodeSnippet(private val mContext: Context) {

    // Do something for marshmallow and above versions
    // do something for phones running an SDK before marshmallow
    val isAboveMarshmallow: Boolean
        get() {
            val currentApiVersion = Build.VERSION.SDK_INT
            return currentApiVersion >= Build.VERSION_CODES.M
        }

    // Do something for marshmallow and above versions
    // do something for phones running an SDK before marshmallow
    val isAboveLollipop: Boolean
        get() {
            val currentApiVersion = Build.VERSION.SDK_INT
            return currentApiVersion >= Build.VERSION_CODES.LOLLIPOP
        }

    /**
     * Returns the current date.
     *
     * @return Current date
     */

    fun getDurationFromUri(uri: Uri?): Long {
        val mediaRetriever = MediaMetadataRetriever()
        mediaRetriever.setDataSource(mContext, uri)
        val time = mediaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        mediaRetriever.release()
        if (time != null)
            return TimeUnit.MILLISECONDS.toSeconds(time.toLong())
        else
            return 0
    }

    fun isDatesAreSame(lastDate: Long, currentDate: Long): Boolean {

        val lastDateDate = Date(lastDate)
        val currentDateDate = Date(currentDate)
        return lastDateDate.date == currentDateDate.date
    }

    fun <T> T.moshiObjToString(type: Class<T>): String {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(type)
        return jsonAdapter.toJson(this)
    }

    fun <T> String.moshiStringToObj(type: Class<T>): T? {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(type)
        return jsonAdapter.fromJson(this)
    }

    @SuppressLint("PackageManagerGetSignatures")
    fun printHashKey(packageManager: PackageManager) {

        try {
            val info = packageManager.getPackageInfo(
                "com.magnum.app",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Timber.d("KeyHash: ${Base64.encodeToString(md.digest(), Base64.DEFAULT)}")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    fun playLikeSound() {
        if (!isPhoneSilent()) {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(mContext, R.raw.like_pop)
            mediaPlayer.start()
        }
    }

    fun isAmazonInstalled(): Boolean {
        val packageManager = mContext.packageManager
        try {
            val applicationInfo =
                packageManager.getPackageInfo("in.amazon.mShop.android.shopping", 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }
    fun isFlipKartInstalled(): Boolean {
        val packageManager = mContext.packageManager
        try {
            val applicationInfo =
                packageManager.getPackageInfo("com.flipkart.android", 0)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }

    fun pauseOtherSounds() {
        val mAudioManager = mContext.getSystemService(AUDIO_SERVICE) as AudioManager
        if (mAudioManager.isMusicActive) {
            mAudioManager.requestAudioFocus(
                null,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }

    }

    private fun isPhoneSilent(): Boolean {
        val mAudioManager = mContext.getSystemService(AUDIO_SERVICE) as AudioManager
        return when (mAudioManager.ringerMode) {
            AudioManager.RINGER_MODE_SILENT -> {
                true
            }
            AudioManager.RINGER_MODE_VIBRATE -> {
                true
            }
            AudioManager.RINGER_MODE_NORMAL -> {
                false
            }
            else -> {
                true
            }
        }
    }

    fun getMimeType(path: String?): String? {
        var type: String? = "image/jpeg" // Default Value
        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    fun makeLinks(textView: TextView, links: Array<String>, clickableSpans: Array<ClickableSpan>) {
        val spannableString = SpannableString(textView.text)
        for (i in links.indices) {
            val clickableSpan = clickableSpans[i]
            val link = links[i]
            val startIndexOfLink = textView.text.toString().indexOf(link)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + link.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = getColor(R.color.transparent)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    fun makeClickableLinks(
        textView: TextView,
        fullText: String?,
        links: Array<String?>,
        clickableSpan: Array<ClickableSpan>
    ) {
        val spannableString = SpannableString(fullText)
        val loweredText = fullText?.toLowerCase(Locale.getDefault())
        for (i in links.indices) {
            val link = links[i]?.toLowerCase(Locale.getDefault())
            link?.let { linkText ->
                val startIndexOfLink = loweredText?.indexOf(linkText)
                startIndexOfLink?.let { it ->
                    if (startIndexOfLink != -1)
                        spannableString.setSpan(
                            clickableSpan[i], it, it + linkText.length,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                }
            }
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = getColor(R.color.transparent)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    fun makeNotificationLink(
        textView: TextView,
        fullText: String,
        highlightText: String?,
        clickableSpan: ClickableSpan
    ) {
        val spannableString = SpannableString(fullText)
        val loweredText = fullText.toLowerCase(Locale.getDefault())
        val startIndexOfLink =
            loweredText.indexOf(highlightText?.toLowerCase(Locale.getDefault()) ?: "")
        if (startIndexOfLink != -1)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink, startIndexOfLink + (highlightText?.length ?: 0),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = getColor(R.color.transparent)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    fun getBitmapUri(bitmap: Bitmap): Uri? {
        val filesDir: File = mContext.filesDir
        val imageFile = File(filesDir, "$currentDate.jpg")

        val os: OutputStream
        return try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.flush()
            os.close()
            convertFileToContentUri(imageFile)

        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    @Throws(Exception::class)
    fun convertFileToContentUri(file: File): Uri {

        //Uri localImageUri = Uri.fromFile(localImageFile); // Not suitable as it's not a content Uri

        val cr = mContext.contentResolver
        val imagePath = file.absolutePath
        val imageName: String? = null
        val imageDescription: String? = null
        val uriString =
            MediaStore.Images.Media.insertImage(cr, imagePath, imageName, imageDescription)
        return Uri.parse(uriString)
    }

    /*public void shareItem(String url) {
    Picasso.with(getApplicationContext()).load(url).into(new Target() {
        @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
            startActivity(Intent.createChooser(i, "Share Image"));
        }
        @Override public void onBitmapFailed(Drawable errorDrawable) { }
        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
    });
}*/

     */
    fun resetFirebaseInstanceId() {
        Thread(Runnable {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId()
                FirebaseInstanceId.getInstance().instanceId
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    fun prepareFilePart(partName: String, filepath: String?): MultipartBody.Part? {
        return if (filepath != null && filepath.isNotEmpty()) {
            val file = File(filepath)
            val requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
            )
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } else {
            null
        }
    }

    fun prepareImageFilePart(partName: String, filepath: String?): MultipartBody.Part? {
        return if (filepath != null && filepath.isNotEmpty()) {
            val file = File(filepath)
            val requestFile = RequestBody.create(
                MediaType.parse("image/*"),
                file
            )
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } else {
            null
        }
    }

    fun prepareVideoFilePart(partName: String, filepath: String?): MultipartBody.Part? {
        return if (filepath != null && filepath.isNotEmpty()) {
            val file = File(filepath)
            val requestFile = RequestBody.create(
                MediaType.parse("video/*"),
                file
            )
            MultipartBody.Part.createFormData(partName, file.name, requestFile)
        } else {
            null
        }
    }

    fun createRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

    fun getSlideFromTopAnimation(): Animation {
        return AnimationUtils.loadAnimation(mContext, R.anim.alerter_slide_in_from_top)

    }

    fun getSlideToTopAnimation(): Animation {
        return AnimationUtils.loadAnimation(mContext, R.anim.alerter_slide_out_to_top)

    }

    fun getTimeFromLong(timeMillis: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        val date = Date(timeMillis)
        dateFormat.format(date)
        return dateFormat.format(date)
    }

    fun getDateFromLong(timeMillis: Long): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = Date(timeMillis)
        dateFormat.format(date)
        return Date(dateFormat.format(date))
    }

    fun getDateStringFromLong(timeMillis: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        val date = Date(timeMillis)
        return if (date.date == currentDate.date)
            "TODAY"
        else
            dateFormat.format(date).toUpperCase(Locale.getDefault())
    }

    fun getTimeAgoString(dateString: String): String {

        try {
            Timber.d("dateString $dateString")
            val utcTimeFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            utcTimeFormat.timeZone = TimeZone.getTimeZone("UTC")
            val past = utcTimeFormat.parse(dateString)
            val now = currentDate
            Timber.d("dateString now ${utcTimeFormat.format(now)}")
            if (past != null) {

                val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
                return when {
                    seconds < 60 -> {

                        if (seconds < 30) {
                            "Just Now"
                        } else
                            "$seconds seconds ago"
                    }
                    minutes < 60 -> {
                        if (minutes < 2)
                            ("$minutes minute ago")
                        else
                            ("$minutes minutes ago")
                    }
                    hours < 24 -> {
                        if (hours < 2)
                            ("$hours hour ago")
                        else
                            ("$hours hours ago")
                    }
                    else -> {
                        if (days < 2)
                            ("$days day ago")
                        else
                            ("$days days ago")

                    }
                }

            } else {
                return ""
            }
        } catch (j: Exception) {
            j.printStackTrace()
            return ""
        } catch (exception: KotlinNullPointerException) {
            return ""
        }
    }

    private val currentDate: Date
        get() = Date(System.currentTimeMillis())


    fun convertTimeFromTo(startDate: String, endDate: String): String {
        try {
            val startDateDate = convertStringToDate(startDate, DATE_TIME_FORMAT)
            val endDateDate = convertStringToDate(endDate, DATE_TIME_FORMAT)
            var startTime = ""
            var endTime = ""
            startDateDate?.let {
                startTime = convertTimeToString(it)
            }
            endDateDate?.let {
                endTime = convertTimeToString(it)
            }

            return "$startTime to $endTime"
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
            return ""
        }
    }


    fun convert24To12Hr(hour: Int, min: Int): String {
        var hourInt = hour
        //String timeHour;
        val timeMin: String = if (min > 9) min.toString() else "0$min"
        var isAm = true
        if (hourInt > 12) {
            isAm = false
            hourInt -= 12
        } else if (hour == 12)
            isAm = false
        // timeHour = hour > 9 ? String.valueOf(hour) : "0" + hour;
        return hourInt.toString() + ":" + timeMin + if (isAm) " am" else " pm"
    }

    fun getPercentStringFromDouble(doubleNumber: Double): String {
        val number4digits: Double = (doubleNumber * 10000.0).roundToInt() / 10000.0
        val number3digits: Double = (number4digits * 1000.0).roundToInt() / 1000.0
        val number2digits: Double = (number3digits * 100.0).roundToInt() / 100.0
        return "$number2digits%"
    }

    fun getPercentStringFromInt(intNumber: Int): String {
        val doubleNumber = intNumber.toDouble()
        val number4digits: Double = (doubleNumber * 10000).roundToInt() / 10000.0
        val number3digits: Double = (number4digits * 1000).roundToInt() / 1000.0
        val number2digits: Double = (number3digits * 100).roundToInt() / 100.0
        return "$number2digits%"
    }

    fun getMillionsFromDouble(doubleNumber: Double): String {
        return String.format("%.2fM", doubleNumber / 1000000.0)
    }

    fun getMillionsFromInt(intNumber: Int): String {
        return String.format("%.2fM", intNumber / 1000000)
    }

    fun getMonthsBetweenRange(): MutableList<String> {

        val monthsList: MutableList<String> = mutableListOf()

        val fromCalendar = Calendar.getInstance()
        fromCalendar.set(Calendar.YEAR, 2012)
        fromCalendar.set(Calendar.MONTH, Calendar.OCTOBER)
        fromCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val tillCalendar = Calendar.getInstance()
        while (fromCalendar.timeInMillis <= tillCalendar.timeInMillis) {
            fromCalendar.add(Calendar.MONTH, 1)
            monthsList.add(convertLongDateToString(fromCalendar.time))
        }
        monthsList.removeAt(monthsList.size - 1)
        return monthsList
    }

    fun getMonthsBetweenRange(startDate: String, endDate: String): MutableList<String> {
        val startingDate = SimpleDateFormat("MMM yyyy", Locale.getDefault()).parse(startDate)
        val endingDate = SimpleDateFormat("MMM yyyy", Locale.getDefault()).parse(endDate)

        val monthsList: MutableList<String> = mutableListOf()

        val fromCalendar = Calendar.getInstance()
        startingDate?.let {
            fromCalendar.time = it
        }

        val tillCalendar = Calendar.getInstance()
        endingDate?.let {
            tillCalendar.time = endingDate
        }
        while (fromCalendar.timeInMillis <= tillCalendar.timeInMillis) {
            fromCalendar.add(Calendar.MONTH, 1)
            monthsList.add(convertLongDateToString(fromCalendar.time))
        }
        monthsList.removeAt(monthsList.size - 1)
        return monthsList
    }

    //    private Bitmap getBitmap(final String imagePath) {
    //
    //        Bitmap bitmap = null;
    //        try {
    //
    //           bitmap = Glide.with(mContext).load("file://" + imagePath).asBitmap().into(500, 500).get();
    //
    //        } catch (InterruptedException | ExecutionException e) {
    //            e.printStackTrace();
    //        }
    //        return bitmap;
    //    }

    @SuppressLint("MissingPermission")
            /**
             * Check which type of connection the device is connected to
             */


    fun getFromToDates(startDate: String?, endDate: String?): String {
        var startDateDate: Date? = null
        var endDateDate: Date? = null
        var finalStartDate = ""
        var finalEndDate = ""
        val df: DateFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        if (!startDate.isNullOrBlank())
            startDateDate = df.parse(startDate)

        if (!endDate.isNullOrBlank())
            endDateDate = df.parse(endDate)

        if (startDateDate != null) {
            finalStartDate = convertShortDateToString(startDateDate)
        }
        if (endDateDate != null) {
            finalEndDate = " to " + convertShortDateToString(endDateDate)
        }

        return finalStartDate + finalEndDate
    }

    fun convertDateToString(date: Date?): String {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date?.let {
            return df.format(it)
        }
        return ""
    }

    fun convertDateToShortString(date: Date?): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        date?.let {
            return df.format(it)
        }
        return ""
    }

    fun convertTodayDateToString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
        return df.format(date)
    }

    fun convertTodayDateToYearString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        return df.format(date)
    }

    fun convertShortDateToString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return df.format(date)
    }

    fun convertTimeToString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
        return df.format(date)
    }

    fun convertLongDateToString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        return df.format(date)
    }

    fun convertSpecificDateFormatToString(date: Date): String {
        val df: DateFormat = SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault())
        return df.format(date)
    }

    fun getWeekMonthNumber(timeStamp: Long, isWeek: Boolean): String {
        val c = Calendar.getInstance()
        c.timeInMillis = timeStamp

        return if (isWeek)
            c.get(Calendar.WEEK_OF_YEAR).toString()
        else
            (c.get(Calendar.MONTH) + 1).toString()
    }

    fun getBiasValue(actualNumber: Int, totalNumber: Int): Float {
        return actualNumber.toFloat() / totalNumber.toFloat()
    }

    fun isTodayLieInBetween(str1: String, str2: String): Boolean {
        try {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val todayStr = formatter.format(Calendar.getInstance().time)
            val todayDate = formatter.parse(todayStr)
            val date1 = formatter.parse(str1)
            val date2 = formatter.parse(str2)

            return todayDate in date1..date2
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    fun convertGlobalDateStringToShortString(date: String): String {
        val convertStringToDate = convertStringToDate(date, DATE_TIME_FORMAT)
        convertStringToDate?.let {
            return convertShortDateToString(it)
        }
        return ""
    }

    fun convertStringToDate(date: String, datePattern: String): Date? {
        val formatter = SimpleDateFormat(datePattern, Locale.getDefault())
        return formatter.parse(date)
    }

    fun isDateAfter(selectedDate: String, fundDate: String): Boolean {
        try {
            val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            val date1 = formatter.parse(selectedDate)
            val date2 = formatter.parse(fundDate)
            date1?.let {
                return it.after(date2)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun isDateBefore(selectedDate: String, fundDate: String): Boolean {
        try {
            val formatter = SimpleDateFormat("MMM yyyy", Locale.getDefault())
            val selectedDt = formatter.parse(selectedDate)
            val fundDt = formatter.parse(fundDate)
            selectedDt?.let {
                return (it.before(fundDt) or (selectedDate == fundDate))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getCalendarTime(time: String): Calendar? {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val calendar = Calendar.getInstance()
            formatter.parse(time)?.apply {
                calendar.time = this
            }
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getCalendarTime(time: Calendar): String? {
        try {
            val formatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            return formatter.format(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarUTCFormat(time: String): String? {
        try {
            val customFormat = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            customFormat.timeZone = TimeZone.getTimeZone("UTC")
            //return new SimpleDateFormat("dd yyyy MMM  hh:mm a", Locale.getDefault()).format(customFormat.parse(time));
            return SimpleDateFormat(
                "dd MMM, hh:mm a",
                Locale.getDefault()
            ).format(customFormat.parse(time)!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarUTCFormat(time: String, fromFormat: String, toFormat: String): String? {
        try {
            val customFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
            return SimpleDateFormat(
                toFormat,
                Locale.getDefault()
            ).format(customFormat.parse(time)!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarForYear(time: String): Calendar? {
        try {

            val formatter = SimpleDateFormat("yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            formatter.parse(time)?.apply {
                calendar.time = this
            }
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarToStandard(time: String): Calendar? {
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            formatter.parse(time)?.apply {
                calendar.time = this
            }
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarToStandard(timeStamp: Long): Calendar? {
        try {
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeStamp
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendarWithTimeOnly(time: String): Calendar {
        try {
            val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            val calendar = Calendar.getInstance()
            formatter.parse(time)?.apply {
                calendar.time = this
            }
            return calendar
        } catch (e: Exception) {
            e.printStackTrace()
            val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
            val calendar = Calendar.getInstance()
            try {
                formatter.parse(time)?.apply {
                    calendar.time = this
                }
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }

            return calendar
        }

    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    fun printDifferenceTillToday(eventDate: String?): String {

        if (!eventDate.isNullOrBlank()) {
            val formatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
            val endingDatedDate = formatter.parse(eventDate)

            val timeNowDate = Calendar.getInstance().time

            if (endingDatedDate?.time != null) {
                //milliseconds
                var different = endingDatedDate.time - timeNowDate.time


                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = different / daysInMilli
                different %= daysInMilli

                val elapsedHours = different / hoursInMilli
                different %= hoursInMilli

                val elapsedMinutes = different / minutesInMilli
                different %= minutesInMilli
                var remaining = "Ends in: "
                if (elapsedDays > 0) {
                    remaining += ("${elapsedDays}d ")
                }
                if (elapsedHours > 0) {
                    remaining += ("${elapsedHours}h ")
                }
                if (elapsedMinutes > 0) {
                    remaining += ("${elapsedMinutes}m")
                }

                Timber.d("differenceInTime $remaining")
                return (remaining)
            }
        }

        return ""

    }

    fun getDayOfMonthMonthAndYear(calendar: Calendar): String {
        val dateString: String
        val formatter = DecimalFormat("00")
        dateString = formatter.format(calendar.get(Calendar.DAY_OF_MONTH).toLong()) + " " +
                monthNameFromInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR)
        return dateString
    }

    fun getDayOfMonthMonthAndYearStd(calendar: Calendar): String {
        val dateString: String
        val month = calendar.get(Calendar.MONTH) + 1
        val formatter = DecimalFormat("00")
        dateString = formatter.format(calendar.get(Calendar.DAY_OF_MONTH).toLong()) + "-" +
                formatter.format(month.toLong()) + "-" + calendar.get(Calendar.YEAR)
        return dateString
    }

    private fun monthNameFromInt(monthInt: Int): String {
        var month = ""
        val dfs = DateFormatSymbols()
        val months = dfs.months
        if (monthInt in 0..11) {
            month = months[monthInt]
        }
        return month.substring(0, 3)
    }

    fun getPastTimerString(calendar: Calendar?): String {
        val time = System.currentTimeMillis() - (calendar?.timeInMillis ?: 0)
        val minutes = time / 60000
        return if (minutes > 59L) {
            val hours = minutes / 60
            if (hours > 24) {
                val days = hours / 24
                if (days > 1) {
                    (days).toString() + "days ago"
                } else {
                    (days).toString() + "days ago"
                }
            } else {
                (hours).toString() + " hours ago"
            }
        } else {
            "less than a minute"
        }
    }

    fun getTimeFromMillisecond(timeStamp: Long): Date {
        return Date(timeStamp)
    }

    fun getOrdinaryTime(time: Time): String {
        return if (time.hour > 12) {
            (formatTime(time.hour - 12) + ":" + formatTime(time.minute)
                    + " " + getAMorPM(time))
        } else ((if (time.hour == 0) (12).toString() else formatTime(time.hour)) + ":"
                + formatTime(time.minute) + " " + getAMorPM(time))

    }


    fun getOrdinaryDate(calendar: Calendar): String {

        val month = calendar.get(Calendar.MONTH) + 1
        val formatter = DecimalFormat("00")
        Timber.d("getOrdinaryDate : ${formatter.format(month.toLong())}")
        return formatter.format(calendar.get(Calendar.DAY_OF_MONTH).toLong()) + "/" + formatter.format(
            month.toLong()
        ) + "/" + calendar.get(
            Calendar.YEAR
        )
    }

    fun getOrdinaryTime(calendar: Calendar): String {

        val min = calendar.get(Calendar.MINUTE)
        //Log.d(TAG,"hours :"+calendar.get(Calendar.HOUR_OF_DAY));
        var meridian = "AM"
        if (calendar.get(Calendar.HOUR_OF_DAY) > 11) {
            meridian = "PM"
        }
        val formatter = DecimalFormat("00")
        return formatter.format(calendar.get(Calendar.HOUR).toLong()) + ":" + formatter.format(min.toLong()) + " " + meridian
    }

    private fun formatTime(time: Int): String {
        return if ((time).toString().length < 2)
            "0$time"
        else
            (time).toString()
    }

    private fun getAMorPM(time: Time): String {
        return if (time.hour > 11) {
            "PM"
        } else
            "AM"
    }

    /**
     * Checking the internet connectivity
     *
     * @return true if the device has a network connection; false otherwise.
     */
    fun hasNetworkConnection(): Boolean {

        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    private fun showGooglePlayDialog(
        context: Context,
        googlePlayServiceListener: OnGooglePlayServiceListener
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Get Google Play Service")
        builder.setMessage("This app won't run without Google Play ServicesData, which are missing from your phone")
        builder.setPositiveButton(
            "Get Google Play Service"
        ) { dialog, _ ->
            googlePlayServiceListener.onInstallingService()
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(("https://play.google.com/store/apps/details?" + "id=com.google.android.gms"))
                )
            )
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "Cancel"
        ) { _, _ -> googlePlayServiceListener.onCancelServiceInstallation() }
        builder.setCancelable(false)
        val alert = builder.create()
        alert.show()
    }

    private fun getSettingsIntent(settings: String): Intent {
        return Intent(settings)
    }

    private fun startActivityBySettings(context: Context, settings: String) {
        context.startActivity(getSettingsIntent(settings))
    }

    private fun startActivityBySettings(context: Context, intent: Intent) {
        context.startActivity(intent)
    }

    fun showGpsSettings(context: Context) {
        startActivityBySettings(context, Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    }

    fun prepareSpinner(data: MutableList<String>): ArrayAdapter<String> =
        ArrayAdapter(
            mContext,
            R.layout.spinner_item,
            data
        )

    fun showNetworkSettings() {
        val chooserIntent = Intent.createChooser(
            getSettingsIntent(Settings.ACTION_DATA_ROAMING_SETTINGS),
            "Complete action using"
        )
        val networkIntents = ArrayList<Intent>()
        networkIntents.add(getSettingsIntent(Settings.ACTION_WIFI_SETTINGS))
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            networkIntents.toTypedArray<Parcelable>()
        )
        startActivityBySettings(mContext, chooserIntent)
    }

    fun isSpecifiedDelay(existingTime: Long, specifiedDelay: Long): Boolean {
        return specifiedDelay >= (Calendar.getInstance().timeInMillis - existingTime)
    }

    fun hideKeyboard(activity: Activity?) {
        activity?.let { it ->
            it.currentFocus?.applicationWindowToken?.apply {
                if (it.getSystemService(Context.INPUT_METHOD_SERVICE) is InputMethodManager) {
                    val imm =
                        it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(this, 0)
                }
            }
        }
    }

    fun showKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus)
            imm.showSoftInputFromInputMethod(
                activity.currentFocus?.applicationWindowToken, 0
            )
    }

    fun isNull(`object`: Any?): Boolean {
        return null == `object` || `object`.toString().compareTo("null") == 0
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (target == null) {
            false
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    fun isValidPassword(target: CharSequence?): Boolean {
        val expression = "^.{8,12}\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        if (target != null) {
            val matcher = pattern.matcher(target)
            return matcher.matches()
        }
        return false
    }

    /**
     * Fetch the drawable object for the given resource id.
     *
     * @param resourceId to which the value is to be fetched.
     * @return drawable object for the given resource id.
     */

    fun getDrawable(resourceId: Int): Drawable? {
        return ResourcesCompat.getDrawable(mContext.resources, resourceId, null)
    }

    /**
     * Fetch the string value from a xml file returns the value.
     *
     * @param resId to which the value has to be fetched.
     * @return String value of the given resource id.
     */

    fun getString(resId: Int): String {
        return mContext.resources.getString(resId)
    }

    /**
     * Fetch the color value from a xml file returns the value.
     *
     * @param colorId to which the value has to be fetched.
     * @return Integer value of the given resource id.
     */

    fun getColor(colorId: Int): Int {
        return ContextCompat.getColor(mContext, colorId)
    }


    private interface OnGooglePlayServiceListener {
        fun onInstallingService()

        fun onCancelServiceInstallation()
    }


    fun loadJSONFromAsset(fileName: String): String? {
        val json: String?
        try {
            val `is` = mContext.assets.open("config/$fileName")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setImageRes(iv: ImageView, @DrawableRes res: Int, @ColorInt colorRes: Int) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && iv.background is RippleDrawable)) {
            val rd = iv.background as RippleDrawable
            rd.setColor(ColorStateList.valueOf(adjustAlpha(colorRes, 0.3f)))
        }
        var d = AppCompatResources.getDrawable(iv.context, res)
        d = DrawableCompat.wrap(d!!.mutate())
        DrawableCompat.setTint(d!!, colorRes)
        iv.setImageDrawable(d)
    }


    companion object {
        private fun adjustAlpha(color: Int, factor: Float): Int {
            val alpha = (Color.alpha(color) * factor).roundToInt()
            val red = Color.red(color)
            val green = Color.green(color)
            val blue = Color.blue(color)
            return Color.argb(alpha, red, green, blue)
        }
    }


    fun setCircleImage(imageView: ImageView, url: String) {
        Glide.with(mContext).load(url)
            .apply(
                RequestOptions.circleCropTransform()
                    .priority(Priority.HIGH)
            )
            .into(imageView)


    }

    fun setImage(imageView: ImageView, url: String) {
        Glide.with(mContext).load(url)
            .apply(
                RequestOptions.centerCropTransform()
                    .priority(Priority.HIGH)
            ).into(imageView)

    }

    fun loadImageFromDrawable(
        imageView: ImageView,
        imageDrawable: Int,
        isSquare: Boolean,
        drawable: Int
    ) {
        if (isSquare) {
            Glide.with(imageView.context)
                .load(imageDrawable)
                .apply(
                    RequestOptions.fitCenterTransform()
                        .placeholder(drawable)
                        .error(drawable)
                )
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(imageDrawable) // Uri of the picture
                .apply(
                    RequestOptions.circleCropTransform()
                        .placeholder(drawable)
                        .error(drawable)
                )
                .into(imageView)
        }
    }

    fun loadImage(imageView: ImageView, imagePath: String, isSquare: Boolean, drawable: Int) {
        if (isSquare) {
            Glide.with(imageView.context)
                .load(imagePath)
                .apply(
                    RequestOptions.fitCenterTransform()
                        .placeholder(drawable)
                        .error(drawable)
                )
                .into(imageView)
        } else {
            Glide.with(imageView.context)
                .load(imagePath) // Uri of the picture
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(drawable)
                        .error(drawable)
                )
                .into(imageView)
        }
    }

    fun getGlide(): RequestManager {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(getDrawable(R.drawable.placeholder_169))
        return Glide.with(mContext)
            .setDefaultRequestOptions(options)
    }

    fun getGlideForDp(): RequestManager {
        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(getDrawable(R.drawable.ic_model_icon))
        return Glide.with(mContext)
            .setDefaultRequestOptions(options)
    }

    fun loadImageFromSDCard(imageView: ImageView, imagePath: String, drawable: Int) {
        Glide.with(imageView.context)
            .load(imagePath)
            .apply(
                RequestOptions
                    .bitmapTransform(RoundedCorners(25))
                    .placeholder(drawable)
                    .error(drawable)
            )
            .into(imageView)
    }

    fun loadImageFromSDCard(imageView: CircularImageView?, imagePath: String) {

        imageView?.let {
            Glide.with(it.context)
                .load(imagePath)
                .into(it)
        }
    }

    fun loadImageFromSDCard(imageView: AppCompatImageView?, imagePath: String) {

        imageView?.let {
            Glide.with(it.context)
                .load(imagePath)
                .into(it)
        }
    }

    fun loadImageFromSDCard(imageView: ImageView, imagePath: Uri, drawable: Int) {
        Glide.with(imageView.context)
            .load(imagePath)
            .apply(
                RequestOptions.bitmapTransform(RoundedCorners(25))
                    .placeholder(drawable)
                    .error(drawable)
            )
            .into(imageView)
    }

    fun loadImageFromSDCardCircleBitmap(imageView: ImageView, imagePath: Uri, drawable: Int) {
        Glide.with(imageView.context)
            .load(imagePath) // Uri of the picture
            .apply(
                RequestOptions().centerCrop()
                    .placeholder(drawable)
                    .error(drawable)
            )
            .into(imageView)
    }

    fun getCalendarTimeToUTC(time: Calendar): String? {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            // formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            return formatter.format(time.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    fun randomColor(): Int {
        val rnd = SecureRandom()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

    private fun getPixelValueFromDimension(i: Int): Int {
        val r = mContext.resources

        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            i.toFloat(),
            r.displayMetrics
        ).toInt()
    }

    fun recyclerViewHorizontalSpace(
        params: RelativeLayout.LayoutParams, adapterPosition: Int,
        spanCount: Int,
        leftWithOutSpace: Int,
        topWithOutSpace: Int,
        rightWithOutSpace: Int,
        bottomWithOutSpace: Int,
        leftWithSpace: Int,
        topWithSpace: Int,
        rightWithSpace: Int,
        bottomWithSpace: Int
    ): RelativeLayout.LayoutParams {
        if ((adapterPosition + 1) % spanCount == 0) {
            params.setMargins(
                getPixelValueFromDimension(leftWithOutSpace),
                getPixelValueFromDimension(topWithOutSpace),
                getPixelValueFromDimension(rightWithOutSpace),
                getPixelValueFromDimension(bottomWithOutSpace)
            )
        } else {
            params.setMargins(
                getPixelValueFromDimension(leftWithSpace),
                getPixelValueFromDimension(topWithSpace),
                getPixelValueFromDimension(rightWithSpace),
                getPixelValueFromDimension(bottomWithSpace)
            )
        }

        return params
    }

    fun setViewError(view: View?, error: String) {
        if (view is AppCompatEditText) {
            view.error = error
            view.requestFocus()
        } else if (view is AppCompatTextView) {
            view.error = error
            view.requestFocus()
        }
    }

    fun togglePasswordVisibility(
        toggleImageView: AppCompatImageView?, passwordEditText:
        AppCompatEditText?, isVisible: Boolean
    ): Boolean {
        if (isVisible) {
            toggleImageView?.setImageResource(R.drawable.ic_eye)
            passwordEditText?.transformationMethod =
                PasswordTransformationMethod.getInstance()
            passwordEditText?.setSelection(passwordEditText.text.toString().trim().length)

        } else {
            toggleImageView?.setImageResource(R.drawable.ic_eye_closed)
            passwordEditText?.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            passwordEditText?.setSelection(passwordEditText.text.toString().trim().length)
        }
        return !isVisible
    }
}