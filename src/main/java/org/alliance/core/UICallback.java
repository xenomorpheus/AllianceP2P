package org.alliance.core;

import org.alliance.core.comm.SearchHit;
import org.alliance.core.node.Friend;
import org.alliance.core.node.Node;

import java.util.List;
import java.util.TreeMap;

/**
 * The interface CoreSubsystem uses to inform a UI and Plug-ins about changes in the state of this Alliance
 * installation.
 * User: maciek
 * Date: 2006-jan-02
 * Time: 21:08:08
 * To change this template use File | Settings | File Templates.
 */
public interface UICallback {

    /**
     * Called when a new friend has been added.
     * @param friend The new friend that was just added
     */
    void signalFriendAdded(Friend friend);

    /**
     * The "share base list" is a list of the root share folder names that a user shares. When Alliance wants to
     * browse the share of a friend this "share base list" is first requested. When the list is received this method
     * is called. The RPC GetShareBaseList is used to get the share base list for a friend.
     * @param friend The friend which this share base list belongs to
     * @param shareBaseNames A list of the root folder names that this user is sharing, each at the index of it's shareBaseIndex
     */
    void receivedShareBaseList(Friend friend, String[] shareBaseNames);

    /**
     * Called when a directory listing is received from a friend. When browsing the share of a friend in Alliance folder
     * information is received one folder at a time. This information is requested when a user expands a folder in the
     * share view. The RPC GetDirectoryListing is used to get the directory listing for a friend.
     * @param friend
     * @param shareBaseIndex
     * @param path
     * @param fileSize
     */
    void receivedDirectoryListing(Friend friend, int shareBaseIndex, String path, TreeMap<String, Long> fileSize);

    /**
     * Called when a node has been updated in some way. Examples are:
     * When user is away from keyboard or comes back.
     * When user goes online/offline.
     * @param node The node that has been updated
     */
    void nodeOrSubnodesUpdated(Node node);

    /**
     * Called when search hits for a search previously made are returned. One call for each friend that returns one or
     * morea search hits. One call is made for all SearchHits for one friend.
     * @param srcGuid The Alliance GUID (network-unique user id) of the friend that is sending us the search results.
     * @param hops Number of hops this search result is from. I always 1.
     * @param hits A list of the search hits.
     */
    void searchHits(int srcGuid, int hops, List<SearchHit> hits);

    /**
     * Called when the status message in the bottom of the Alliance main window is to be updated.
     * @param s The new status message to be displayed
     */
    void statusMessage(String s);

    /**
     * Called when the status message in the bottom of the Alliance main window is to be updated and its important.
     * @param s The new status message to be displayed
     * @param b important indicator
     */
    void statusMessage(String s, boolean b);

    /**
     * Is the UI visible or is Alliance running in background mode?
     * @return True if the UI is visible and Alliance is not running in background mode
     */
    boolean isUIVisible();

    /**
     * Called when something has happened that is considered important enough to bring the Alliance window to front.
     */
    void toFront();

    /**
     *
     * @param ui
     */
    void newUserInteractionQueued(NeedsUserInteraction ui);

    /**
     * Used by plug-ins - when the user interface is started a new UICallback implementation is set on the
     * CoreSubsystem - this will remove any UICallbacks implemented by plug-ins that where hooked on the CoreSubsystem.
     * A plugin implements this method and re-registers its UICallback implementetion using CoreSubsystem.addUICallback
     * with the CoreSubsystem in this call.
     *
     * Look in the HelloWorldExample plugin for how this is done.
     */
    void callbackRemoved();

    /**
     * When the first download ever for this Alliance installation is finished this method is called.
     */
    void firstDownloadEverFinished();

    /**
     * When interesting network event occur, for example when a user goes online, this method should be called. Not
     * used in many placed right now tho.
     * @param event The network event as a human readable string, for example "User X when online".
     */
    void logNetworkEvent(String event);

    /**
     * Called once for every trace message generated by the Alliance code.
     * @param level Trace level: trace, debug, info, warn, error
     * @param message The trace message
     * @param stackTrace Stack trace to where this trace message was generated
     */
    void trace(int level, String message, Exception stackTrace);

    /**
     * Called when an error occurs. Used to display an error dialog in the UI.
     * @param e The error.
     * @param Source Source from where the error occured, can be lots of stuff, for example a Connection object.
     */
    void handleError(Throwable e, Object Source);

    /**
     * Not used in the current version of Alliance.
     * @param node The node that a RCP could not be routed to.
     */
    void noRouteToHost(Node node);

    /**
     * This is invoked when a RPC of type PlugInCommunication is recevied. It is used by plugins to communicate
     * between friends. To send a message of this type to another friend use:
     * <code>
     * if (friend.isConnected()) {
     *   friend.getFriendConnection().send(new PlugInCommunication("MYBOT\tmy data"));
     * }
     * The friend on the other side will then receive an event of this type with the String data containing
     * "MYBOT\tmy data".
     * @param source The source friend that sent this message
     * @param data The data contained within this message. It's up to the creator of the plugin to decide what this
     * should contain. When doing a plugin make sure to prefix the data-string with something unique for your plugin.
     * Otherwise you might receive communication ment for some other plugin running on this installation of Alliance.
     */
    void pluginCommunicationReceived(Friend source, String data);
}
