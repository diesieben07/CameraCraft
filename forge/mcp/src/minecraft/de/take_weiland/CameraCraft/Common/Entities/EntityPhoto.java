package de.take_weiland.CameraCraft.Common.Entities;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import de.take_weiland.CameraCraft.Common.CameraCraft;
import de.take_weiland.CameraCraft.Common.IPhotoSource;
import de.take_weiland.CameraCraft.Common.PhotoInformation;
import de.take_weiland.CameraCraft.Common.PhotoSizeAmountInfo;
import de.take_weiland.CameraCraft.Common.Gui.GuiScreens;
import de.take_weiland.CameraCraft.Common.Items.CameraCraftItem;
import de.take_weiland.CameraCraft.Common.Items.ItemPhotoStorage;

public class EntityPhoto extends Entity implements IEntityAdditionalSpawnData, IPhotoSource {
    private int tickCounter1;

    /** the direction the painting faces */
    public int direction;
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public PhotoInformation info;

    public EntityPhoto(World world) {
        super(world);
        this.tickCounter1 = 0;
        this.direction = 0;
        this.yOffset = 0.0F;
        this.setSize(0.5F, 0.5F);
    }

    public EntityPhoto(World world, PhotoInformation info, int posX, int posY, int posZ, int dir) {
        this(world);
        this.xPosition = posX;
        this.yPosition = posY;
        this.zPosition = posZ;
        this.info = info;
        this.setDirection(dir);
    }
    
    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
    	// TODO!
    	if (player instanceof EntityPlayerMP && getDistanceToEntity(player) < 1.58F) {
    		info.teleport((EntityPlayerMP) player);
    	}
    }
    
    private void dropAsItem() {
    	ItemStack stack = new ItemStack(CameraCraftItem.photo, 1, 1);
    	ItemPhotoStorage.addPhoto(stack, info);
    	worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, stack));
    }

    @Override
    protected void entityInit() {}

    @Override
    public boolean interact(EntityPlayer player) {
    	if (!worldObj.isRemote) {
    		player.openGui(CameraCraft.instance, GuiScreens.VIEW_PHOTOS_ENTITY.toGuiId(), worldObj, entityId, 0, 0);
    	}
    	return true;
    }
    
    public void setDirection(int dir) {
        this.direction = dir;
        this.prevRotationYaw = this.rotationYaw = (float)(dir * 90);
        float sizeX = info.getSizeX() * 16;
        float sizeY = info.getSizeX() * 16;
        float sizeX2 = info.getSizeX() * 16;

        if (dir != 0 && dir != 2)
        {
            sizeX = 0.5F;
        }
        else
        {
            sizeX2 = 0.5F;
        }

        sizeX /= 32.0F;
        sizeY /= 32.0F;
        sizeX2 /= 32.0F;
        float xPosition = (float)this.xPosition + 0.5F;
        float yPosition = (float)this.yPosition + 0.5F;
        float zPosition = (float)this.zPosition + 0.5F;
        float var8 = 0.5625F;

        if (dir == 0)
        {
            zPosition -= var8;
        }

        if (dir == 1)
        {
            xPosition -= var8;
        }

        if (dir == 2)
        {
            zPosition += var8;
        }

        if (dir == 3)
        {
            xPosition += var8;
        }

        if (dir == 0)
        {
        	xPosition -= this.func_70517_b(info.getSizeX() * 16);
        }

        if (dir == 1)
        {
        	zPosition += this.func_70517_b(info.getSizeX() * 16);
        }

        if (dir == 2)
        {
        	xPosition += this.func_70517_b(info.getSizeX() * 16);
        }

        if (dir == 3)
        {
        	zPosition -= this.func_70517_b(info.getSizeX() * 16);
        }

        yPosition += this.func_70517_b(info.getSizeY() * 16);
        this.setPosition((double)xPosition, (double)yPosition, (double)zPosition);
        float var9 = -0.00625F;
        this.boundingBox.setBounds((double)(xPosition - sizeX - var9), (double)(yPosition - sizeY - var9), (double)(zPosition - sizeX2 - var9), (double)(xPosition + sizeX + var9), (double)(yPosition + sizeY + var9), (double)(zPosition + sizeX2 + var9));
    }

    private float func_70517_b(int par1) {
        return par1 == 32 ? 0.5F : (par1 == 64 ? 0.5F : 0.0F);
    }

    @Override
    public void onUpdate() {
        if (this.tickCounter1++ == 100 && !this.worldObj.isRemote)
        {
            this.tickCounter1 = 0;

            if (!this.isDead && !this.onValidSurface())
            {
                this.setDead();
                dropAsItem();
            }
        }
    }

    public boolean onValidSurface() {
        if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
        	int var1 = info.getSizeX();
        	int var2 = info.getSizeY();
            int var3 = this.xPosition;
            int var4 = this.yPosition;
            int var5 = this.zPosition;

            if (this.direction == 0) {
            	var3 = MathHelper.floor_double(this.posX - (double)((float)info.getSizeX() * 16 / 32.0F));
            }

            if (this.direction == 1) {
            	var5 = MathHelper.floor_double(this.posZ - (double)((float)info.getSizeX() * 16 / 32.0F));
            }

            if (this.direction == 2) {
            	var3 = MathHelper.floor_double(this.posX - (double)((float)info.getSizeX() * 16 / 32.0F));
            }

            if (this.direction == 3) {
            	var5 = MathHelper.floor_double(this.posZ - (double)((float)info.getSizeX() * 16 / 32.0F));
            }

            var4 = MathHelper.floor_double(this.posY - (double)((float)info.getSizeX() * 16 / 32.0F));

            for (int i = 0; i < var1; ++i) {
                for (int j = 0; j < var2; ++j) {
                    Material material;

                    if (this.direction != 0 && this.direction != 2) {
                        material = this.worldObj.getBlockMaterial(this.xPosition, var4 + j, var5 + i);
                    } else {
                        material = this.worldObj.getBlockMaterial(var3 + i, var4 + j, this.zPosition);
                    }

                    if (!material.isSolid()) {
                        return false;
                    }
                }
            }

            List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox);
            Iterator var10 = var9.iterator();
            Entity var11;

            do {
                if (!var10.hasNext()) {
                    return true;
                }

                var11 = (Entity)var10.next();
            }
            while (!(var11 instanceof EntityPhoto));

            return false;
        }
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, int par2) {
        if (!this.isDead && !this.worldObj.isRemote) {
            this.setDead();
            this.setBeenAttacked();
            EntityPlayer sourcePlayer = null;

            if (damageSource.getEntity() instanceof EntityPlayer) {
                sourcePlayer = (EntityPlayer)damageSource.getEntity();
            }

            if (sourcePlayer != null && sourcePlayer.capabilities.isCreativeMode) {
                return true;
            }

            dropAsItem();
        }

        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        nbt.setByte("Dir", (byte)this.direction);
        nbt.setInteger("TileX", this.xPosition);
        nbt.setInteger("TileY", this.yPosition);
        nbt.setInteger("TileZ", this.zPosition);
        nbt.setCompoundTag("info", info.createNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        this.direction = nbt.getByte("Dir");
        this.xPosition = nbt.getInteger("TileX");
        this.yPosition = nbt.getInteger("TileY");
        this.zPosition = nbt.getInteger("TileZ");
        this.info = PhotoInformation.createFromNBT(nbt.getCompoundTag("info"));
        
        this.setDirection(this.direction);
    }

    @Override
    public void moveEntity(double xMove, double yMove, double zMove) {
        if (!this.worldObj.isRemote && !this.isDead && xMove * xMove + yMove * yMove + zMove * zMove > 0.0D) {
            this.setDead();
            dropAsItem();
        }
    }

    @Override
    public void addVelocity(double xVel, double yVel, double zVel) {
        if (!this.worldObj.isRemote && !this.isDead && xVel * xVel + yVel * yVel + zVel * zVel > 0.0D)
        {
            this.setDead();
            dropAsItem();
        }
    }

	@Override
	public void writeSpawnData(ByteArrayDataOutput data) {
		data.writeByte(direction);
		data.writeInt(xPosition);
		data.writeInt(yPosition);
		data.writeInt(zPosition);
		info.writeToData(data);
	}

	@Override
	public void readSpawnData(ByteArrayDataInput data) {
		direction = data.readByte();
		xPosition = data.readInt();
		yPosition = data.readInt();
		zPosition = data.readInt();
		
		info = PhotoInformation.createFromData(data);
		
		setDirection(direction);
	}

	/* Photo Source part */	
	@Override
	public void nameChanged(int photoIndex, String newName) {
		if (photoIndex == 0) {
			info.setName(newName);
		}
	}

	@Override
	public boolean canViewPhotos(EntityPlayer player) {
		return !player.isDead && !isDead && getDistanceSqToEntity(player) <= 64;
	}

	@Override
	public PhotoInformation getPhotoInformation(String photoId) {
		return photoId.equals(info.getPhotoId()) ? info : null;
	}

	@Override
	public void deletePhoto(int photoIndex) { }

	@Override
	public boolean canDelete() {
		return false;
	}

	@Override
	public boolean canPrint() {
		return false;
	}

	@Override
	public void addToPrintQueue(List<PhotoSizeAmountInfo> photos) { }

	@Override
	public PhotoInformation getPhotoInformation(int index) {
		return index == 0 ? info : null;
	}

	@Override
	public int numPhotos() {
		return 1;
	}

	@Override
	public GuiScreens getScreenTypeViewPhotos() {
		return GuiScreens.VIEW_PHOTOS_ENTITY;
	}

	@Override
	public int getX() {
		return (int)posX;
	}

	@Override
	public int getY() {
		return (int)posY;
	}

	@Override
	public int getZ() {
		return (int)posZ;
	}

	@Override
	public void startViewing(EntityPlayer player) { }

	@Override
	public void stopViewing(EntityPlayer player) { }
}