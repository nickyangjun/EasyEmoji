package org.nicky.libeasyemoji.EasyInput.interfaces;

/**
 * Created by nickyang on 2017/3/23.
 */

public interface IPanelContentManager {
    void addContent(String tag, Object content);
    void openPanel(String tag);
    void closePanel();
    String getCurrentPanelDisplayTag();
    void removeContent(String tag);
}
