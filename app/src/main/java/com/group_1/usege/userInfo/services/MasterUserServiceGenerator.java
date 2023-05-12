package com.group_1.usege.userInfo.services;

import android.content.Context;

import com.group_1.usege.R;
import com.group_1.usege.authen.repository.TokenRepository;
import com.group_1.usege.utilities.api.BaseAuthServiceGenerator;
import com.group_1.usege.utilities.api.BaseServiceGenerator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class MasterUserServiceGenerator extends BaseAuthServiceGenerator<MasterUserService> {
    @Inject
    MasterUserServiceGenerator(@ApplicationContext Context context, TokenRepository tokenRepository) {
        super(tokenRepository, context.getResources(), R.string.api_version, R.string.service_master_user);
    }

    @Override
    protected Class<MasterUserService> getServiceClass() {
        return MasterUserService.class;
    }
}
