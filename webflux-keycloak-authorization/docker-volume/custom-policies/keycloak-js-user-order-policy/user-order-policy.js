//-- HOST/orders/by-user/{userId}/list

var tokenUserId = $evaluation.getContext().getIdentity().getId();
var uri = $evaluation.getPermission().getClaims().get('uri_claim').iterator().next();
var uriUserId = uri.split('/')[3];

print("##############################################");
print("####  tokenUserId: " + tokenUserId);
print("####  urlUserId: " + uriUserId);
print("##############################################");

if(uriUserId.equals(tokenUserId)){
	$evaluation.grant();
}else{
	$evaluation.deny();
}
