package com.akash.audiowidget

import android.content.Intent
import android.widget.RemoteViewsService

class MyWidgetRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return WidgetRemoveViewFactory(applicationContext, intent)
    }
}
