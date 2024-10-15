var tokenUserId = $evaluation.getContext().getIdentity().getId();
var uri = $evaluation.getPermission().getClaims().get('uri_claim').iterator().next();
var uriUserId = uri.split('/')[3];

if(uriUserId.equals(tokenUserId)){
	$evaluation.grant();
}else{
	$evaluation.deny();
}
