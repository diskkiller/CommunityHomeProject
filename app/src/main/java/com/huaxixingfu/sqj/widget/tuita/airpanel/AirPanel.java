package com.huaxixingfu.sqj.widget.tuita.airpanel;

/**
 * @version 1.0.0
 */

public interface AirPanel {
    interface Sub {
        void openPanel();

        void closePanel();

        boolean isOpen();
    }

    interface Boss extends Sub {
        void setup(PanelListener panelListener);

        void setOnStateChangedListener(OnStateChangedListener listener);
    }

    interface PanelListener {
        void requestHideSoftKeyboard();
    }

    interface OnStateChangedListener {
        void onPanelStateChanged(boolean isOpen);

        void onSoftKeyboardStateChanged(boolean isOpen);
    }
}
