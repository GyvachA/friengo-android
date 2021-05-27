package com.hypnex.friengo.utils

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun Context.toast(stringId: Int) = Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
fun Fragment.toast(message: String) = activity?.toast(message)
fun Fragment.toast(stringId: Int) = activity?.toast(stringId)
