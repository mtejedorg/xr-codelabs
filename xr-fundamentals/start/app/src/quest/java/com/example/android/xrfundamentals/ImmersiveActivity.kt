 // (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

 package com.example.android.xrfundamentals

import android.net.Uri
import android.os.Bundle
import androidx.compose.ui.platform.ComposeView
import com.example.android.xrfundamentals.ui.theme.XRFundamentalsTheme
import com.meta.spatial.compose.ComposeViewPanelRegistration
import com.meta.spatial.core.Entity
import com.meta.spatial.core.Pose
import com.meta.spatial.core.SpatialFeature
import com.meta.spatial.core.Vector3
import com.meta.spatial.okhttp3.OkHttpAssetFetcher
import com.meta.spatial.runtime.NetworkedAssetLoader
import com.meta.spatial.runtime.ReferenceSpace
import com.meta.spatial.runtime.SceneMaterial
import com.meta.spatial.toolkit.AppSystemActivity
import com.meta.spatial.toolkit.Material
import com.meta.spatial.toolkit.Mesh
import com.meta.spatial.toolkit.PanelRegistration
import com.meta.spatial.toolkit.Transform
import com.meta.spatial.toolkit.UIPanelSettings
import com.meta.spatial.toolkit.createPanelEntity
import com.meta.spatial.vr.VRFeature
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImmersiveActivity : AppSystemActivity() {
  private val activityScope = CoroutineScope(Dispatchers.Main)

  override fun registerFeatures(): List<SpatialFeature> {
    return listOf(
        VRFeature(this)
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    NetworkedAssetLoader.init(
        File(applicationContext.getCacheDir().canonicalPath), OkHttpAssetFetcher())

    // TODO: register the LookAt system and component

  }

  override fun onSceneReady() {
    super.onSceneReady()
    scene.setViewOrigin(0.0f, 0.0f, 0.0f)
    scene.enableHolePunching(true)
    scene.setReferenceSpace(ReferenceSpace.LOCAL_FLOOR)

    activityScope
        .launch { glXFManager.inflateGLXF(Uri.parse("scenes/AppScene.glxf"), keyName = "scene") }
        .invokeOnCompletion {
          val composition = glXFManager.getGLXFInfo("scene")
          val environment = composition.getNodeByName("environment").entity
          val environmentMesh = environment.getComponent<Mesh>()
          environmentMesh.defaultShaderOverride = SceneMaterial.UNLIT_SHADER
          environment.setComponent(environmentMesh)

            Entity.createPanelEntity(
                R.id.xr_fundamentals,
                Transform(Pose(Vector3(0.0f, 0.0f, -1.0f))),
            )

          // TODO: get the robot and the basketBall entities from the composition

          // TODO: add the LookAt component to the robot so it points at the basketBall

        }
    Entity.create(
        listOf(
            Mesh(Uri.parse("mesh://skybox")),
            Material().apply {
              baseTextureAndroidResourceId = R.drawable.skydome
              unlit = true
            }))
  }

  override fun registerPanels(): List<PanelRegistration> {
    return listOf(
        ComposeViewPanelRegistration(
            R.id.xr_fundamentals,
            composeViewCreator = { _, ctx ->
                ComposeView(ctx).apply { setContent { XRFundamentalsApp() } }
            },
            settingsCreator = { _ ->
                UIPanelSettings()
            },
        )
    )
  }
}
