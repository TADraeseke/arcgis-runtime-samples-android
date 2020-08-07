/*
 * Copyright 2020 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgisruntime.sample.ogcapifeaturelayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.data.Field
import com.esri.arcgisruntime.data.OgcFeatureCollectionTable
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.security.AuthenticationManager
import com.esri.arcgisruntime.security.DefaultAuthenticationChallengeHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    AuthenticationManager.setTrustAllSigners(true)

    val ogcFeatureCollectionTable =
      OgcFeatureCollectionTable("http://rt-geoservera.esri.com:8080/geoserver/ogc/features", "tiger:poly_landmarks")
    ogcFeatureCollectionTable.featureRequestMode =
      ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE
    ogcFeatureCollectionTable.loadAsync()

    val featureLayer = FeatureLayer(ogcFeatureCollectionTable)
    featureLayer.loadAsync()
    featureLayer.addDoneLoadingListener {
      if (featureLayer.loadStatus != LoadStatus.LOADED) {
        Log.e("MainActivity", "FEATURE LAYER NOT LOADED: ${featureLayer.loadError}")
      }
    }

    val featureQueryResultFuture = ogcFeatureCollectionTable.populateFromServiceAsync(
      QueryParameters(),
      true,
      null)

    featureQueryResultFuture.addDoneListener {
      try {
        mapView.setViewpointAsync(Viewpoint(featureLayer.fullExtent))
      } catch (e: Exception) {
        Log.e("MainActivity", "aaaaaaaahhhhhhhh")
      }
    }

    mapView.map = ArcGISMap(Basemap.createImagery())
  }
}
