function fn() {
  var env = karate.env;
  karate.log('karate.env system property:' + env);
  karate.configure('logPrettyRequest',true);
  karate.configure('logPrettyResponse',true);
  karate.configure('connectTimeout',30000);
  karate.configure('readTimeout',30000);
  var config = {
    baseUrl: 'https://96e36031a4274c4db37f716589692724.smartappqa.com/EnterpriseDesktop/api/v2/budgets/f1ee589c-2960-4b30-a655-66e84fbcffce/lineitems?sessionId=c464c4c755e045728d240fe0f35d525b'

  };
  return config;
}