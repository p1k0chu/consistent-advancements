package com.github.p1k0chu.mcmod.consistent_advancements.mixin;

import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.TreeNodePosition;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(TreeNodePosition.class)
public class TreeNodePositionMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementNode;children()Ljava/lang/Iterable;"))
    public Iterable<AdvancementNode> getChildren(AdvancementNode instance) {
        Iterator<AdvancementNode> children = instance.children().iterator();
        List<AdvancementNode> sortedChildren = new ArrayList<>();

        while(children.hasNext()) {
            sortedChildren.add(children.next());
        }

        sortedChildren.sort((node1, node2) -> {
            Optional<String> name1 = node1.holder().value().name().map(Component::getString);
            Optional<String> name2 = node2.holder().value().name().map(Component::getString);

            if(name1.isEmpty()) return -1;
            if(name2.isEmpty()) return 1;

            return name1.get().compareTo(name2.get());
        });
        return sortedChildren;
    }
}