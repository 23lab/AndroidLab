# 测试AActivity用startActivityForResult拉起B以后，A被finish掉，B返回后能否调用A的onActivityResult

## 如果A不是Launch Activity，Afinish掉以后，onActivityResult不再被调用

## 如果把A设置为Launch Activity，同样， 说明跟AActivity是否为Lauch无关

经过测试，如果A Activity是被手动finish掉的，则不会回调onActivityResult
如果A Activity是给系统Kill掉的，整个进程没了， 则会重新启动并回调onActivityResult，模拟时需要把A和B分别放到两个proccess里面


