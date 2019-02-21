package com.example.chapter1.dao.notuse;

import com.example.chapter1.domain.User;

//업그레이드 정책을 분리하여 상황에 따라 다르게 이용 가능하다.
public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
