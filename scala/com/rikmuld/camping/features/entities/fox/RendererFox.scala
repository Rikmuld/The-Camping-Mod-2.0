package com.rikmuld.camping.features.entities.fox

import com.rikmuld.camping.Library._
import net.minecraft.client.renderer.entity.{RenderLiving, RenderManager}
import net.minecraft.entity.EntityAgeable
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.{Side, SideOnly}
import org.lwjgl.opengl.GL11

@SideOnly(Side.CLIENT)
class RendererFox(manager: RenderManager) extends RenderLiving[EntityFox](manager, new ModelFox(), .4f) {
  override def doRender(entity: EntityFox, d0: Double, d1: Double, d2: Double, f: Float, f1: Float) {
    GL11.glPushMatrix()
    if (entity.asInstanceOf[EntityAgeable].isChild) GL11.glTranslatef(0, -0.75F, 0)
    super.doRender(entity, d0, d1, d2, f, f1)
    GL11.glPopMatrix()
  }
  protected override def getEntityTexture(entity: EntityFox): ResourceLocation = new ResourceLocation(TextureInfo.MODEL_FOX)
}