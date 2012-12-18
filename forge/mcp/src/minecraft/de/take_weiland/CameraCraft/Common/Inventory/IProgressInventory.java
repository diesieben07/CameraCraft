package de.take_weiland.CameraCraft.Common.Inventory;

/**
 * An interface to be implemented by Inventories that provide a Progressbar
 * @author diesieben07
 *
 */
public interface IProgressInventory {
	/**
	 * gets the current time of the process (the higher the number the more of the progress is done, if {@code getProcessTime()} = {@link #getProcessDuration getProcessDuration()} the progress is done)
	 * @return the current process time
	 */
	public int getProcessTime();

	/**
	 * sets the process time, used on the client
	 * @param time the current process time
	 */
	public void setProcessTime(int time);

	/**
	 * gets the total Duration of the Progress (if {@link #getProcessTime getProcessTime()} = {@code getProcessDuration()} the progress is done)
	 * @return the total duration
	 */
	public int getProcessDuration();

	/**
	 * sets the total Duration of the Progress, used on the client
	 * @param time the total duration
	 */
	public void setProcessDuration(int time);
}
