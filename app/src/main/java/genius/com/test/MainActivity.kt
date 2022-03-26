package genius.com.test

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.viewpager.widget.ViewPager
import genius.com.test.fragments.MyFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val EXTRA_FRAGMENT_NUMBER = "extra_fragment_number"
private const val SAVED_FRAGMENTS_COUNT = "save_fragments_count"


private var notificationId = 0
val savedNotificationIds = mutableMapOf<Int, MutableList<Int>?>()

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder


    private var fragmentsCount = 0
    lateinit var viewPager: ViewPager
    lateinit var viewPagerAdapter: ViewPagerAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        val savedFragmentsCount = sharedPref.getInt(SAVED_FRAGMENTS_COUNT, 1)

        viewPagerAdapter = ViewPagerAdapter(this)
        view_pager.adapter = viewPagerAdapter

        repeat(savedFragmentsCount) {
            addFragment()
        }

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    fun addFragment() {
        fragmentsCount++
        viewPagerAdapter.addFragment(MyFragment.newInstance(fragmentsCount))
        view_pager.currentItem = fragmentsCount - 1
    }

    fun removeLastFragment() {
        if (fragmentsCount > 1) {
            viewPagerAdapter.removeLastFragment()

            fragmentsCount--
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(getString(R.string.channel_id), name, importance).apply {
                    description = descriptionText
                }

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun createNewNotification(number: Int) {


        val contentIntent = Intent(this, MainActivity::class.java).apply {
            putExtra(EXTRA_FRAGMENT_NUMBER, number)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val contentPendingIntent = PendingIntent.getActivity(
            this,
            notificationId,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder =
            NotificationCompat.Builder(applicationContext, getString(R.string.channel_id))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text, number))
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_avatar
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentPendingIntent)
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }

        if (savedNotificationIds[number] == null) {
            savedNotificationIds[number] = mutableListOf(notificationId)
        } else {
            savedNotificationIds[number]?.add(notificationId)
        }

        notificationId++
    }

    override fun onStop() {
        super.onStop()

        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(SAVED_FRAGMENTS_COUNT, fragmentsCount)
            apply()
        }
    }

}