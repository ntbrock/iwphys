class IWPProblemServerRequest < ActionWebService::Struct
  member :authkey, :string
  member :directory, :string
  member :username, :string
  member :password, :string
end
