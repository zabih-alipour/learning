//-- LOG

print("##############################################");
print("####  $evaluation.getContext() ");
print($evaluation.getContext());
print("##############################################");

print("##############################################");
print("####  $evaluation.getContext().getAttributes()  ");
print($evaluation.getContext().getAttributes());
print("##############################################");

print("##############################################");
print("####  $evaluation.getContext().getAttributes().toMap()  ");
print($evaluation.getContext().getAttributes().toMap());
print("##############################################");

print("##############################################");
print("####  $evaluation.getPermission().getClaims() ");
print($evaluation.getPermission().getClaims());
print(JSON.stringify($evaluation.getPermission().getClaims()));
print("##############################################");

print("##############################################");
print("####  $evaluation.getPermission().getScopes() ");
print($evaluation.getPermission().getScopes());
print("##############################################");

print("##############################################");
print("####  $evaluation.getPermission().isGranted() ");
print($evaluation.getPermission().isGranted());
print("##############################################");


print("##############################################");
print("####  $evaluation.getPermission().getResource() ");
print($evaluation.getPermission().getResource());
print("##############################################");

print("##############################################");
print("####  $evaluation.getPermission() ");
print($evaluation.getPermission());
print("##############################################");

$evaluation.grant();

