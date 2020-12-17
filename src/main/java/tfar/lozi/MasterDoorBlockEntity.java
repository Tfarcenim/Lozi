package tfar.lozi;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MasterDoorBlockEntity extends TileEntity {

    private String code;

    private static final String s = "code";

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.code = compound.getString(s);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (code != null)
        compound.setString(s, code);
        return compound;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
