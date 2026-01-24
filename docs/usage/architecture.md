# Architecture

Guiy encourages following Android's well-documented [app architecture standards](https://developer.android.com/topic/architecture). I highly recommend getting accustomed to the data and UI layers.

Due to how compose is structured, we can't reuse many classes it provides directly. However, Guiy does its best to implement the minimum needed to follow the laid out recommendations (ex. we provide a simple ViewModel implementation, similar Modifier patterns, navigation, etc...)
