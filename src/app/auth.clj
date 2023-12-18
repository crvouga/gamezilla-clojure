
(defprotocol AuthStore
  (find-user [this])
  (insert-user [this user]))

(defrecord AuthStoreImplPostgres [user]
  AuthStore
  (find-user [this] user)
  (insert-user [this user] (AuthStoreImplPostgres. user)))



(def auth-store (AuthStoreImplPostgres))

(find-user auth-store)
`