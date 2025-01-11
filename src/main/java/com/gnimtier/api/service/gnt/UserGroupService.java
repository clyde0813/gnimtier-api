package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;

import java.util.List;
import java.util.Map;

public interface UserGroupService {
    Map<String, List<UserGroupResponseDto>> getUserGroups(String userId);
}
