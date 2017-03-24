package org.nicky.libeasyemoji.EasyInput.interfaces;


import org.nicky.libeasyemoji.EasyInput.IMEPanelLayout;

/**
 * Created by nickyang on 2017/3/23.
 */

public interface IPanelLayout {
    IMEPanelLayout getPanel();
    void changeHeight(int panelHeight);
    int getHeight();
    void openPanel();
    void closePanel();
    void addOnPanelListener(OnPanelListener listener);
    boolean isVisible();
    void setHide();
    void handleShow();
}
