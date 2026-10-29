package com.strawberry.client.entity;

import com.strawberry.StrawberryMod;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cow;

/**
 * Рендерер клубничной коровы и клубничного телёнка.
 *
 * <p>В 1.21.9 у коровы отдельная модель для малышей ({@link ModelLayers#COW_BABY}).
 * Если использовать обычный {@code MobRenderer} только со взрослой моделью,
 * телёнок рисуется взрослой моделью в полный размер — визуально «вырастает»
 * сразу после превращения. {@link AgeableMobRenderer} сам выбирает модель
 * по флагу {@code isBaby} из состояния рендера.</p>
 */
public class StrawberryCowRenderer extends AgeableMobRenderer<Cow, CowRenderState, CowModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            StrawberryMod.MOD_ID,
            "textures/entity/strawberry_cow/strawberry_cow.png"
    );

    public StrawberryCowRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new CowModel(context.bakeLayer(ModelLayers.COW)),
                new CowModel(context.bakeLayer(ModelLayers.COW_BABY)),
                0.7F
        );
    }

    @Override
    public CowRenderState createRenderState() {
        return new CowRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(CowRenderState state) {
        return TEXTURE;
    }
}
