package rfinder.Util;

import rfinder.Hazeron.StarMap;

public interface StarMapReceiver {

    void onStarMapUpdate(StarMap starMap);

    void onStarMapClear();
}
