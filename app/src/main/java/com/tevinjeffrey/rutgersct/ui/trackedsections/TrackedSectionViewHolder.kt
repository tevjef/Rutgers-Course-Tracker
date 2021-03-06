package com.tevinjeffrey.rutgersct.ui.trackedsections

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tevinjeffrey.rutgersct.R
import com.tevinjeffrey.rutgersct.ui.sectioninfo.SectionInfoViewHolder
import com.tevinjeffrey.rutgersct.ui.utils.CircleView

class TrackedSectionViewHolder private constructor(
    parent: View,
    instructors: TextView,
    val courseTitleText: TextView,
    sectionNumberBackground: CircleView,
    sectionTimeContainer: ViewGroup) : SectionInfoViewHolder(parent, instructors, sectionNumberBackground, sectionTimeContainer) {

  fun setCourseTitle(subjectNumber: String, courseName: String) {
    courseTitleText.text = subjectNumber + " | " + courseName
  }

  companion object {

    fun newInstance(parent: View): TrackedSectionViewHolder {
      val sectionInfoVH = SectionInfoViewHolder.newInstance(parent)
      val courseTitleText = parent.findViewById<TextView>(R.id.courseTitleText)
      val instructors = sectionInfoVH.instructors
      val sectionNumberBackground = sectionInfoVH.sectionNumberBackground
      val sectionTimeContainer = sectionInfoVH.sectionTimeContainer
      return TrackedSectionViewHolder(
          parent,
          instructors,
          courseTitleText,
          sectionNumberBackground,
          sectionTimeContainer
      )
    }
  }
}
