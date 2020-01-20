package com.careagle.sdk.base

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.components.support.RxFragment

abstract class BasePresenter {
    protected fun getActivityLifecycleProvider(baseView: IBaseView): RxAppCompatActivity {
        return baseView as RxAppCompatActivity
    }

    protected fun getFragmentLifecycleProvider(baseView: IBaseView): RxFragment {
        return baseView as RxFragment
    }
}