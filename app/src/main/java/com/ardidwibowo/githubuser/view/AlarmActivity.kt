package com.ardidwibowo.githubuser.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ardidwibowo.githubuser.alarm.Receiver.AlarmReceiver
import com.ardidwibowo.githubuser.alarm.ReminderPreference
import com.ardidwibowo.githubuser.model.Reminder
import com.ardidwibowo.githubuser.databinding.ActivityAlarmBinding

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAlarmBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var reminder: Reminder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val reminderPreference = ReminderPreference(this)
        binding.switch1.isChecked = reminderPreference.getReminder().isReminded

        alarmReceiver = AlarmReceiver()
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                saveReminder(true)
                alarmReceiver.setRepeatAlarm(this, "RepeatingAlarm","06:00", "Github reminder")
            }else{
                saveReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }

    }

    private fun saveReminder(state : Boolean){
        val reminderPreference = ReminderPreference(this)
        reminder = Reminder()

        reminder.isReminded = state
        reminderPreference.setReminder(reminder)

    }
}