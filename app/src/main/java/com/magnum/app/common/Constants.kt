package com.magnum.app.common

import com.magnum.app.common.Constants.CommonKeys.Companion.ONE_MINUTE

interface Constants {

    interface CommonKeys {
        companion object {
            const val SPLASH_TIME_OUT = 2000L
            const val EXIT_TIMEOUT = 2000L
            const val ONE_SECOND = 1000L
            const val THIRTY_SECONDS = 30000L
            const val ONE_MINUTE = 60000L
            const val ZERO = 0
            const val MINUS_ONE = -1
        }
    }

    interface FragmentCodes {
        companion object {
            const val CONTEST_NOT_ENTERED_FRAGMENT = 1
            const val ALL_CONTEST_FRAGMENT = 2
            const val NEW_CONTEST_FRAGMENT = 3
            const val CONTEST_ENTERED_FRAGMENT = 4
            const val CONTEST_PROGRESS_FRAGMENT = 5
        }
    }

    interface TitleType {

        companion object {
            const val CONTEST_PAGE = 0
            const val FEEDS_PAGE = 1
            const val ADD_FEED_PAGE = 2
            const val PROFILE_PAGE = 3
            const val MORE_PAGE = 4
        }
    }

    interface FaceBookKeys {
        companion object {
            const val USER_PHOTO = "user_photos"
            const val USER_EMAIL = "email"
            const val USER_BIRTHDAY = "user_birthday"
            const val USER_PUBLIC_PROFILE = "public_profile"
            const val FB_FIELDS = "fields"
            const val FB_FIELDS_KEYS = "first_name,last_name,email,id,picture"
        }
    }

    interface SocialRequestCodes {
        companion object {
            const val FB_REQUEST_CODE = 64206
        }
    }

    interface FragmentTags {
        companion object {
            const val ADD_FEED_FRAGMENT_TAG = "ADD_FEED_FRAGMENT"
            const val CONTEST_NOT_ENTERED_FRAGMENT_TAG = "CONTEST_NOT_ENTERED_FRAGMENT"
            const val ALL_CONTEST_FRAGMENT_TAG = "ALL_CONTEST_FRAGMENT"
            const val NEW_CONTEST_FRAGMENT_TAG = "NEW_CONTEST_FRAGMENT"
            const val CONTEST_ENTERED_FRAGMENT_TAG = "CONTEST_ENTERED_FRAGMENT"
            const val CONTEST_PROGRESS_FRAGMENT_TAG = "CONTEST_PROGRESS_FRAGMENT"
            const val CONTEST_ROOT_FRAGMENT_TAG = "CONTEST_ROOT_FRAGMENT"
            const val FEEDS_FRAGMENT_TAG = "FEEDS_FRAGMENT"
            const val MORE_FRAGMENT_TAG = "MORE_FRAGMENT"
            const val PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT"

        }
    }

    interface ViewPagerPositions {
        companion object {
            const val FOLLOWERS_FRAGMENT_POSITION = 0
            const val FOLLOWING_FRAGMENT_POSITION = 1

        }
    }

    interface FeedsOrigin {
        companion object {
            const val ALL_FEEDS = 1
            const val PROFILE_FEEDS = 2
            const val MY_FEEDS = 3
            const val COMMUNITY_FEEDS = 4
            const val MAX_FEEDS_RECYCLE_COUNT = 99
        }
    }

    interface FeedType {
        companion object {
            const val IMAGE_FEED = "image"
            const val VIDEO_FEED = "video"
            const val IMAGE_TEXT = "image"
            const val VIDEO_TEXT = "video"
        }
    }

    interface SocialMediaType {
        companion object {
            const val FACEBOOK_NAME = "facebook"
            const val INSTAGRAM_NAME = "Instagram"
            const val FACEBOOK_INTEGER = 1
            const val INSTAGRAM_INTEGER = 2

        }
    }

    interface FeedsCount {
        companion object {
            const val TEXT_FEED = 0
            const val SINGLE_MEDIA_FEED = 1
        }
    }

    interface MediaMaxCount {
        companion object {
            const val MAX_MEDIA_COUNT = 10
            const val GALLERY_COLUMN_COUNT = 3
        }
    }

    interface PasswordPattern {
        companion object {
            const val PASSWORD_PATTERN =
                "^.{8,14}\$"
        }
    }

    interface NotificationType {
        companion object {
            const val APPLIED = 1
            const val FIRST_ROUND = 0
            const val TOP_50 = 2
            const val TOP_10 = 3
            const val TOP_3 = 4
            const val WON = 5
            const val NOT_SELECTED = 6
            const val REVIEW = 7
            const val FREE_SAMPLE = 8
            const val VOTES = 9
            const val LIKE = 10
            const val PROFILE_UPDATE = 11
            const val GENERAL_NOTIFICATION = 12
            const val BROADCAST_MESSAGE = 13
        }
    }

    interface NotificationTypeString {
        companion object {
            const val APPLIED = "APPLIED"
            const val FIRST_ROUND = "FIRST_ROUND"
            const val TOP_50 = "TOP_50"
            const val TOP_10 = "TOP_10"
            const val TOP_3 = "TOP_3"
            const val WON = "WON"
            const val NOT_SELECTED = "NOT_SELECTED"
            const val REVIEW = "REVIEW"
            const val FREE_SAMPLE = "FREE_SAMPLE"
            const val VOTES = "VOTES"
            const val LIKE = "LIKE"
            const val PROFILE_UPDATE = "PROFILE_UPDATE"
            const val GENERAL_NOTIFICATION = "GENERAL_NOTIFICATION"
            const val BROADCAST_MESSAGE = "BROADCAST_MESSAGE"
            const val CHAT_MESSAGE = "CHAT_MESSAGE"

        }
    }

    interface CommunityTabTitles {
        companion object {
            const val ALL_CONTESTANTS = "All Contestants"
            const val COMMUNITY = "Community"
        }
    }

    interface ContestSelectionFlags {
        companion object {
            const val PENDING_APPROVAL = 1
            const val APPROVED = 2
            const val REJECTED = 3
        }
    }

    interface BundleKey {
        companion object {
            const val USER_ID = "userId"
            const val IS_FOLLOWING = "isFollowing"
            const val PROFILE_DATA = "profileData"
            const val EVENT_DATA = "eventData"
            const val APPROVAL_STATUS = "approvalStatus"
            const val MOBILE_NUMBER = "mobileNumber"
            const val FEED_ID = "feedId"
            const val FEED_ORIGIN = "feedOrigin"
            const val ACCESS_TOKEN = "accessToken"
            const val TIME_LINE_DATA = "timeLineData"
            const val FOLLOWERS_FOLLOWING_COUNT = "followersFollowingCount"
            const val URI_LIST = "uriList"
            const val SELECTED_VIDEO_URI = "selectedVideoUri"
            const val CONTEST_ID = "contestId"
            const val CATEGORY_ID = "categoryId"
            const val TERMS_CONDITIONS_FOR_PHOTO = "termsConditionsForPhoto"
            const val TERMS_CONDITIONS = "termsConditions"
            const val PRODUCT_URL = "productUrl"
            const val EVENT_NAME = "eventName"
            const val RESULT_TYPE = "resultType"
            const val MESSAGE = "message"
            const val IS_VOTED = "isVoted"
        }
    }

    interface SendBird {
        companion object {
            // const val SB_APP_ID = "9F174968-EBE7-404E-8C2A-9096DB5F351A"
            //test account
            //const val SB_APP_ID = "BDC69D52-6086-4F9F-AF5C-BF04F5941DBC"
            //production account
            const val SB_APP_ID = "B511D58D-77EE-44A1-BEDB-D193B6A20B1D"
            /*
            const val SB_APP_ID = "60C8F27A-6A1A-433E-ADBB-B470FEF8A53D"
            const val SB_API_TOKEN = "6dbd2ef2d214d7e3e7c967c5a69d7e3e57b2f1ed"
            const val SB_API_URL = "https://api-60C8F27A-6A1A-433E-ADBB-B470FEF8A53D.sendbird.com"
            */
            const val USERNAME = "username"
            const val USER_COVER_URL = "profileurl"
            const val PHONE_NUMBER = "phonenumber"
            const val USER_MAIL_ID = "emailid"
            const val USER_NICK_NAME = "nickname"
            const val USER_TYPE = "type"
            const val USER_TYPE_ANDROID = "android"
            const val ADMIN_USER_ID = "Admin"
            const val USER_TYPE_CUSTOMER = "customer"
            const val USER_TYPE_ADMIN = "admin"
            const val USER_ID = "userId"

        }
    }

    interface GlobalTimeFormat {
        companion object {
            const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        }
    }

    interface ApiHeaderKey {
        companion object {
            const val AUTHORIZATION = "x-access-token"
            const val PLATFORM = "platform"
        }
    }

    interface ApiPlatform {
        companion object {
            const val WEB = "1"
            const val ANDROID = "2"
            const val IOS = "3"
        }
    }

    interface HttpErrorMessage {
        companion object {
            const val INTERNAL_SERVER_ERROR =
                "Our server is under maintenance. We will resolve shortly!"
            const val FORBIDDEN = "Seems like you haven't permitted to do this operation!"
            const val TIMEOUT = "Unable to connect to server. Please try after sometime"
            const val UNAUTHORIZED = "Session expired. Please try login again."
        }
    }

    interface PostReviewKeys {
        companion object {
            const val PRODUCT_ID = "productId"
            const val IS_LISTICLE_REVIEW = "listicleReview"
            const val IS_VIDEO_REVIEW = "videoReview"
            const val PRODUCT_ATTENTION_DESC = "productAttentionDescription"
            const val PRODUCT_REACTION_DESC = "productReactionDescription"
            const val PRODUCT_INGRED_DESC = "productIngredientDescription"
            const val PRODUCT_BENEFITS_DESC = "productBenefitDescription"
            const val FULL_PRODUCT_DESC = "fullProductDescription"
            const val PRODUCT_PRICE = "productPrice"
            const val SELFIE_PHOTO = "selfiePhoto"
            const val PRODUCT_PHOTO = "productPhoto"
            const val VIDEO_REVIEW_FILE = "videoReviews"
        }
    }

    interface WhichImagePicker {
        companion object {
            const val SELFIE_PHOTO_CONSTANT = 1
            const val PRODUCT_PHOTO_CONSTANT = 2
        }
    }

    interface PermissionCode {
        companion object {
            const val WRITE_PERMISSION_CODE = 203
            const val VIDEO_TRIMMER_REQUEST_CODE = 205
            const val IMAGE_VIDEO_TRIMMER_REQUEST = 206

        }
    }

    interface TimeLineStatus {
        companion object {
            const val NOT_IN_PROGRESS = 0
            const val IN_PROGRESS = 1
            const val DONE_PROGRESS = 2
            const val REJECTED_PROGRESS = 3

        }
    }

    interface Pagination {
        companion object {
            const val MAX_ITEM_LIMIT = 10
            const val PAGE_START_INDEX = 1
            const val TOP_THREE = 3
        }
    }

    interface RequestCodes {
        companion object {
            const val NOTIFICATION_REQUEST = 341
            const val BECOME_PARTICIPANT = 1232
            const val INSTAGRAM_SIGN_IN = 235
            const val USER_DETAILS = 4323
            const val SHARE_CONTESTANT_CODE = 531
            const val ADD_THOUGHTS_REQUEST = 674

        }
    }

    interface InternalHttpCode {
        companion object {
            const val FAILURE_CODE = 404
            const val BAD_REQUEST_CODE = 400
            const val UNAUTHORIZED_CODE = 401
            const val INTERNAL_SERVER_ERROR_CODE = 500
            const val TIMEOUT_CODE = 504
        }
    }

    interface PreferenceKey {
        companion object {
            const val SHARED_PREF_NAME = "Skinella"
            const val TOKEN = "token"
            const val PROFILE_DATA = "profile_data"
            const val NOTIFICATION_COUNT = "notificationCount"
        }
    }

    interface MaxValues {
        companion object {
            const val MAX_VIDEO_DURATION: Long = ONE_MINUTE

        }
    }

    interface SpinneData {
        companion object {
            const val SPINNER_DEFAULT_TEXT = "-Select-"

        }
    }

    interface MultiPartKeys {
        companion object {
            const val MEDIA = "media"
            const val DESCRIPTION = "description"
            const val NAME = "name"
            const val EMAIL = "email"
            const val DOB = "dob"
            const val BIO = "bio"

        }
    }

    interface AnimationDirection {
        companion object {
            const val LEFT = 1
            const val RIGHT = 2

        }
    }
}