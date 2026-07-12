# DeepTech
A Minecraft Mod Themed Sculk

# to Re_Construction
 - 教程中会出现自定义的方块和物品, 按照原版的思路, 继承的分别是`Item`和`Block`类, 但是目前在有`Nebula Libs`的条件下可以直接继承`BasicItem`和`BasicBlock`, 同样的也拥有`BasicBlockEntity`等
 - 请看看`dev.celestiacraft.deep_tech.api.client.ItemModelGen`中的方法, 这里面包含了几乎所有的物品模型生成方法, 可以省去你自己写模型的麻烦, 具体可以参考我留下的`SCULK_CHUNK`
 - 在每次`runData`时可以运行`DeepTech/Tasks/forgegradle runs/runData`, `runClient`同理
 - 在开发中难免会遇到需要设置`Setter`和`Getter`, 我为你配置了`Lombok`, 直接在对应的参数上添加注解`@Getter`或`@Setter`即可
 - 因为不知名的原因, `runData`完成时会出现类似于`[23:44:35] [main/INFO] [minecraft/HashCache]: Caching: total files: 4, old count: 0, new count: 5, removed stale: 0, written: 5`的提示, 这个无需担心. 直接取消运行即可
 - 生成后的文件会在`src/generated/resources/`下
 - 你在`runClient`的时候可能会发现Mod比较多, 这是因为添加lib时必须把他的前置lib全部加上, 但是你`build`后在别的地方也可以跑
 - `build`后请确保自己选择的版本没有`-slim`后缀, 完整的版本会将前置库内置在Mod文件内