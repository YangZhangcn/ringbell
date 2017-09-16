package com.zhangyang.ringbell

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by zhang on 2017/9/15.
 */
class ScrollerTestFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context,R.layout.fragment_scroller,null)
    }

}