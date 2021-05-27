package core.rest.services

import core.rest.model.User
import core.utils.ApiUtils
import core.utils.FileUtils
import core.utils.StringUtils
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RootApiService {
    var ROOT_PATH = ApiUtils.getDomain()
    var stringUtils = StringUtils
    var fileUtils = FileUtils
}